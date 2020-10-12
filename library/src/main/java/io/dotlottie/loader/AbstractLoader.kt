package io.dotlottie.loader

import android.content.Context
import androidx.lifecycle.lifecycleScope
import io.dotlottie.loader.defaults.DefaultDotLottieCache
import io.dotlottie.loader.defaults.DefaultDotLottieConverter
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieConfig
import io.dotlottie.loader.models.DotLottieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Prototype for loaders,
 * subclasses must provide implementation for [loadInternal]
 */
abstract class AbstractLoader(protected val context: Context) {


    /**
     * Configuration. Either per instance or default to global
     * config set via [DotLottieLoader.setConfig]
     */
    private var dlCallConfig: DotLottieConfig = DotLottieLoader.globalConfig


    /**
     * Factory method to set a custom configuration
     * for this request only
     */
    fun withConfig(config: DotLottieConfig) {
        dlCallConfig = config.apply {
            cacheManager?.initialize(context)
        }
    }


    // accessors to cache stuff, fallback to globals
    // the ugliness because some jackass might've init a global config
    // with nulls? no but that's on you dude
    private val cacheManager: DotLottieCache
        get() = dlCallConfig.cacheManager
            ?:DotLottieLoader.globalConfig.cacheManager
            ?:DefaultDotLottieCache.apply { initialize(context) }

    private val cacheStrategy: DotLottieCacheStrategy
        get() = dlCallConfig.cacheStrategy
            ?:DotLottieLoader.globalConfig.cacheStrategy
            ?:DotLottieCacheStrategy.DISK

    private val converter: DotLottieConverter
        get() = dlCallConfig.converter
            ?: DotLottieLoader.globalConfig.converter
            ?: DefaultDotLottieConverter()



    /**
     * commit actual load, launch coroutine scope and
     * pass up the result
     */
    fun load(listener: DotLottieResult, cacheKey:String? = null) {

        (context.lifecycleOwner()?.lifecycleScope ?:GlobalScope)
            .launch(Dispatchers.IO) {
                try {

                    val key = cacheKey?:getDefaultCacheName()
                    val result = cacheManager
                        .fromCache(key, cacheStrategy)
                        ?: loadInternal()
                            .apply {
                                cacheManager.putCache(this, key, cacheStrategy)
                            }

                    // update on UI
                    withContext(Dispatchers.Main) {
                        listener.onSuccess(result)
                    }

                } catch (e: Exception) {

                    withContext(Dispatchers.Main) { listener.onError(e) }
                }
            }
    }


    /**
     * get resource entry name for raw resource
     */
    protected abstract val fileEntryName: String


    /**
     * internal loader function to be overridden
     */
    protected abstract suspend fun loadInternal(): DotLottie


    /**
     * return default cache key for loader type
     */
    protected abstract suspend fun getDefaultCacheName(): String


    /**
     * parse input stream
     */
    protected suspend fun parseInputStream(inputStream: InputStream): DotLottie? =
        converter.parseFileInputStream(inputStream, fileEntryName)


    /**
     * parse zip input stream
     */
    protected suspend fun parseZipInputStream(inputStream: InputStream): DotLottie? =
        converter.parseZipInputStream(ZipInputStream(inputStream))


}
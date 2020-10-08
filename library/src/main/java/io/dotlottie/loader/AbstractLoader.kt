package io.dotlottie.loader

import android.content.Context
import io.dotlottie.loader.defaults.DefaultDotLottieConverter
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieConfig
import io.dotlottie.loader.models.DotLottieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.zip.ZipInputStream

/**
 * Prototype for loaders,
 * subclasses must provide implementation for [loadInternal]
 */
abstract class AbstractLoader(protected val context: Context) {


    private var dlConverter: DotLottieConverter = DefaultDotLottieConverter()

    /**
     * Configuration. Either per instance or default to global
     * config set via [DotLottieLoader.setConfig]
     */
    private var dlCallConfig: DotLottieConfig = DotLottieLoader.globalConfig

    /**
     * get resource entry name for raw resource
     */
    protected abstract val fileEntryName: String


    /**
     * set a custom [DotLottieConverter] to parse
     * results of this request
     */
    fun withConverter(converter: DotLottieConverter) {
        dlConverter = converter
    }


    /**
     * set a custom configuration
     * for this request only
     */
    fun withConfig(config: DotLottieConfig) {
        dlCallConfig = config
    }


    /**
     * commit actual load, launch coroutine scope and
     * pass up the result
     */
    fun load(listener: DotLottieResult, cacheKey:String? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val key = cacheKey?:getDefaultCacheName()

                val result = dlCallConfig
                    .cacheManager
                    .fromCache(key, dlCallConfig.cacheStrategy)
                    ?: loadInternal()
                        .apply {

                            // give the cache manager a chance to cache this,
                            // since it missed the cache on the first run and this
                            // was loaded and parsed down the stack
                            dlCallConfig
                                .cacheManager
                                .putCache(this, key, dlCallConfig.cacheStrategy)
                        }

                launch(Dispatchers.Main){ listener.onSuccess(result) }

            } catch (e: Exception) {
                launch(Dispatchers.Main){ listener.onError(e) }
            }
        }
    }


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
    protected fun parseInputStream(inputStream: InputStream): DotLottie? =
        dlConverter.parseFileInputStream(inputStream)


    /**
     * parse zip input stream
     */
    protected fun parseZipInputStream(inputStream: InputStream): DotLottie? =
        dlConverter.parseZipInputStream(ZipInputStream(inputStream))


}
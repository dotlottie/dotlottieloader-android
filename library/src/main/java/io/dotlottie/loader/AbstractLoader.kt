package io.dotlottie.loader

import android.content.Context
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieConverter
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
     * get resource entry name for raw resource
     */
    protected abstract val fileEntryName: String


    /**
     * set a custom DotLottieConverter
     */
    fun setConverter(converter: DotLottieConverter) {
        dlConverter = converter
    }


    /**
     * commit actual load, launch coroutine scope and
     * pass up the result
     */
    fun load(listener: DotLottieResult) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = loadInternal()
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
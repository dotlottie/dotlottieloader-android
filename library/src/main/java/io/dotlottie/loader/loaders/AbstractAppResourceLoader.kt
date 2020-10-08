package io.dotlottie.loader.loaders

import android.content.Context
import io.dotlottie.loader.isZipCompressed
import io.dotlottie.loader.models.DotLottie
import java.io.InputStream

/**
 * Abstracted loader for internal resources,
 * as Raw and Asset files behave similarly enough.
 * See concrete [AssetLoader] and [ResLoader]
 */
abstract class AbstractAppResourceLoader (context: Context): AbstractLoader(context) {

    /**
     * get inputstream for raw resource
     */
    protected abstract val resInputStream: InputStream


    /**
     * get resource entry name for raw resource
     */
    protected abstract val resEntryName: String


    /**
     * open and parse app internal resources
     */
    private fun parseResFileSpec(): DotLottie? =
        when (resInputStream.isZipCompressed()) {
            true -> parseZipInputStream(resInputStream)
            false -> parseInputStream(resInputStream)
        }


    override fun loadInternal(): DotLottie {
        parseResFileSpec()?.let {
            return it
        }

        throw IllegalArgumentException("Unable to parse resource")
    }
}
package io.dotlottie.loader.loaders

import android.content.Context
import androidx.annotation.RawRes
import io.dotlottie.loader.AbstractLoader
import io.dotlottie.loader.isZipCompressed
import io.dotlottie.loader.lastSegmentName
import io.dotlottie.loader.models.DotLottie
import java.io.InputStream

/**
 * Abstracted loader for internal resources,
 * as Raw and Asset files behave similarly enough.
 */
abstract class AppResourceLoader (context: Context): AbstractLoader(context) {

    /**
     * get inputstream for raw resource
     */
    protected abstract val resInputStream: InputStream


    /**
     * open and parse app internal resources
     */
    private suspend fun parseResFileSpec(): DotLottie? =
        when (resInputStream.isZipCompressed()) {
            true -> parseZipInputStream(resInputStream)
            false -> parseInputStream(resInputStream)
        }


    override suspend fun loadInternal(): DotLottie {
        val res = parseResFileSpec()

        // throw exception if we're not done
        if(res!=null)
            return res
        else
            throw IllegalArgumentException("Unable to parse resource")
    }
}



/**
 * Concrete [AppResourceLoader] that loads an app asset
 */
class AssetLoader(context: Context, private val assetName: String)
    : AppResourceLoader(context) {

    override val resInputStream: InputStream
        get() = context.assets.open(assetName)


    override val fileEntryName: String
        get() = assetName.lastSegmentName()


    override suspend fun getDefaultCacheName(): String = assetName

}


/**
 * Concrete [AppResourceLoader] that loads a raw resource
 */
class ResLoader(context: Context, @RawRes private val resId: Int)
    : AppResourceLoader(context) {


    override val resInputStream: InputStream
        get() = context.resources.openRawResource(resId)


    override val fileEntryName: String
        get() = context.resources.getResourceEntryName(resId)


    override suspend fun getDefaultCacheName(): String = fileEntryName

}
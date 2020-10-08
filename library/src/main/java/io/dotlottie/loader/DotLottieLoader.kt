package io.dotlottie.loader

import android.content.Context
import androidx.annotation.RawRes
import io.dotlottie.loader.loaders.AssetLoader
import io.dotlottie.loader.loaders.NetworkLoader
import io.dotlottie.loader.loaders.ResLoader

/**
 * Factory method to build a loader stack
 * actual work is done by the loaders and converters
 */
class DotLottieLoader private constructor(private val context: Context) {



    /*
     * ----- The stuff below are mostly Builder methods ----
     */


    /**
     * loads animation from assets
     * might throw [java.io.IOException]
     */
    fun fromAsset(assetName: String) = AssetLoader(context, assetName)


    /**
     * loads animation from raw resource
     * might throw [java.io.IOException]
     */
    fun fromRaw(@RawRes resId: Int) = ResLoader(context, resId)


    /**
     * loads animation from a URL
     */
    fun fromUrl(url: String) = NetworkLoader(context, url)


    companion object {

        /**
         * Factory method to get an instance of the loader
         * @param context context for this instantiation
         */
        fun with(context: Context) = DotLottieLoader(context)

    }
}
package io.dotlottie.loader

import android.content.Context
import androidx.annotation.RawRes

/**
 * DotLottieLoader handles loading dotLottie files from
 * network, or app resources (raw or assets)
 */
class DotLottieLoader private constructor(val context: Context) {


    /**
     * loads animation from assets
     */
    fun fromAsset(assetName: String) {

    }


    /**
     * loads animation from raw resource
     */
    fun fromRaw(@RawRes resId: Int) {

    }


    /**
     * loads animation from a URL
     */
    fun fromUrl(url: String) {

    }

    companion object {

        /**
         * Factory method to get an instance of the loader
         * @param context context for this instantiation
         */
        fun with(context: Context) = DotLottieLoader(context)

    }
}
package io.dotlottie.loader

import android.content.Context
import androidx.annotation.RawRes
import io.dotlottie.loader.defaults.DefaultDotLottieCache
import io.dotlottie.loader.defaults.DefaultDotLottieConverter
import io.dotlottie.loader.loaders.AssetLoader
import io.dotlottie.loader.loaders.NetworkLoader
import io.dotlottie.loader.loaders.ResLoader
import io.dotlottie.loader.models.DotLottieConfig

/**
 * Factory method to build a loader stack
 * actual work is done by the loaders and converters
 */
class DotLottieLoader private constructor(private val context: Context) {


    /**
     * loads animation from assets
     * might throw [java.io.IOException]
     */
    fun fromAsset(assetName: String) = AssetLoader(context, assetName)

    // note to future self: Do we not like kotlin delegates?

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

        internal var globalConfig: DotLottieConfig =
            DotLottieConfig(
                cacheStrategy = DotLottieCacheStrategy.DISK,
                cacheManager = DefaultDotLottieCache,
                converter = DefaultDotLottieConverter()
            )

        /**
         * Factory method to get an instance of the loader
         * @param context context for this instantiation
         */
        @JvmStatic
        fun with(context: Context):DotLottieLoader = DotLottieLoader(context)

        /**
         * Exposed setter to alter the global configuration.
         * This can be different from the call specific
         * configurations
         */
        @JvmStatic
        fun setConfig(configuration: DotLottieConfig) {
            globalConfig = configuration
        }

    }
}
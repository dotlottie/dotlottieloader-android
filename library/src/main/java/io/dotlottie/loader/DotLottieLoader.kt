package io.dotlottie.loader

import android.content.Context
import android.content.res.AssetFileDescriptor
import androidx.annotation.RawRes
import io.dotlottie.loader.models.DotLottieResult
import okhttp3.OkHttpClient

/**
 * DotLottieLoader handles loading dotLottie files from
 * network, or app resources (raw or assets)
 */
class DotLottieLoader private constructor(private val context: Context) {

    /*
     * internal state holder for load state
     */
    private data class LOADSPEC (
        val url: String?=null,
        @RawRes val raw: Int?=null,
        val asset: String?=null
    )

    /*
     * state vars for load
     */
    private var overrideClient: OkHttpClient? = null
    private var loadSpec: LOADSPEC? = null


    fun load(listener: DotLottieResult) {
        if(loadSpec?.raw!=null || loadSpec?.asset!=null) {
            loadInternalFileSpec(loadSpec?.raw, loadSpec?.asset, listener)
        } else if(loadSpec?.url!=null) {
            //todo: network
            //todo: cache configs
        } else {
            listener.onError(IllegalArgumentException("No loadable targets specified"))
        }
    }


    /**
     * open and parse app internal resources
     */
    private fun loadInternalFileSpec(@RawRes raw: Int?, asset: String?, listener: DotLottieResult) {
        val fd: AssetFileDescriptor

        try {
            // try init the file description or fail if both are null
            when {
                raw != null -> fd = context.resources.openRawResourceFd(raw)
                asset != null -> fd = context.assets.openFd(asset)
                else -> listener.onError(IllegalArgumentException())
            }

        } catch (e: Exception) {
            listener.onError(e)
        }

    }


    /*
     * ----- The stuff below are mostly Builder methods ----
     */

    /**
     * set a custom OkHttpClient to passover to
     * the NetworkFetcher
     */
    fun setClient(client: OkHttpClient) {
        overrideClient = client
    }


    /**
     * loads animation from assets
     * might throw [java.io.IOException]
     */
    fun fromAsset(assetName: String):DotLottieLoader {
        loadSpec = LOADSPEC(asset = assetName)
        return this
    }


    /**
     * loads animation from raw resource
     * might throw [java.io.IOException]
     */
    fun fromRaw(@RawRes resId: Int):DotLottieLoader {
        loadSpec = LOADSPEC(raw = resId)
        return this
    }


    /**
     * loads animation from a URL
     */
    fun fromUrl(url: String):DotLottieLoader {
        loadSpec = LOADSPEC(url = url)
        return this
    }

    companion object {

        /**
         * Factory method to get an instance of the loader
         * @param context context for this instantiation
         */
        fun with(context: Context) = DotLottieLoader(context)

    }
}
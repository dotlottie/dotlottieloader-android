package io.dotlottie.loader

import android.content.Context
import androidx.annotation.RawRes
import io.dotlottie.loader.models.DotLottieResult
import okhttp3.OkHttpClient
import java.lang.IllegalArgumentException

/**
 * DotLottieLoader handles loading dotLottie files from
 * network, or app resources (raw or assets)
 */
public class DotLottieLoader private constructor(private val context: Context) {

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
        if(loadSpec!=null) {

        } else {
            listener.onError(IllegalArgumentException("No loadable targets specified"))
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
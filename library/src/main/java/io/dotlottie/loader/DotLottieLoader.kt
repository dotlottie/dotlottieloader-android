package io.dotlottie.loader

import android.content.Context
import androidx.annotation.RawRes
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieConverter
import io.dotlottie.loader.models.DotLottieResult
import okhttp3.OkHttpClient
import java.io.InputStream
import java.lang.Exception
import java.util.zip.ZipInputStream

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
    private var dlConverter: DotLottieConverter = DefaultDotLottieConverter()


    fun load(listener: DotLottieResult) {

        try {

            var file: DotLottie? = null

            if (loadSpec?.raw != null || loadSpec?.asset != null) {
                file = parseResFileSpec(loadSpec?.raw, loadSpec?.asset)

            } else if (loadSpec?.url != null) {
                //todo: network
                //todo: cache configs
            } else {
                listener.onError(IllegalArgumentException("No loadable targets specified"))
            }

            // return the dotlottie
            if(file!=null) {
                listener.onSuccess(file)
            } else {
                listener.onError(IllegalArgumentException("Error parsing input"))
            }

        } catch (e: Exception) {
            listener.onError(e)
        }
    }


    /**
     * open and parse app internal resources
     */
    private fun parseResFileSpec(@RawRes raw: Int?, asset: String?): DotLottie? =
        when (getResInputStream(raw, asset).isZipCompressed()) {
            true -> dlConverter.parseZipInputStream(ZipInputStream(getResInputStream(raw,asset)))
            false -> dlConverter.parseFileInputStream(getResInputStream(raw,asset))
        }


    /**
     * get inputstream for internal resource file
     */
    private fun getResInputStream(@RawRes raw: Int?, asset: String?): InputStream = when {
        raw != null -> context.resources.openRawResource(raw)
        asset != null -> context.assets.open(asset)
        else -> throw IllegalArgumentException()
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
     * set a custom DotLottieConverter
     */
    fun setConverter(converter: DotLottieConverter) {
        dlConverter = converter
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
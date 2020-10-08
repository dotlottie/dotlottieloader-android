package io.dotlottie.loader.loaders

import android.content.Context
import io.dotlottie.loader.models.DotLottie
import okhttp3.OkHttpClient

class NetworkLoader(context: Context, private val url: String): AbstractLoader(context) {


    private var overrideClient: OkHttpClient? = null

    /**
     * set a custom OkHttpClient to passover to
     * the NetworkFetcher
     */
    fun setClient(client: OkHttpClient) {
        overrideClient = client
    }


    override fun loadInternal(): DotLottie {
        TODO("Not yet implemented")
    }


}
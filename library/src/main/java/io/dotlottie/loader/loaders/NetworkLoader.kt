package io.dotlottie.loader.loaders

import android.content.Context
import io.dotlottie.loader.AbstractLoader
import io.dotlottie.loader.lastSegmentName
import io.dotlottie.loader.models.DotLottie
import okhttp3.OkHttpClient

class NetworkLoader(context: Context, private val url: String): AbstractLoader(context) {

    override val fileEntryName: String
        get() = url.lastSegmentName()


    private var overrideClient: OkHttpClient? = null

    /**
     * set a custom OkHttpClient to passover to
     * the NetworkFetcher
     */
    fun setClient(client: OkHttpClient) {
        overrideClient = client
    }



    override suspend fun loadInternal(): DotLottie {
        TODO("Not yet implemented")
    }


}
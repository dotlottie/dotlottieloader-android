package io.dotlottie.loader.loaders

import android.content.Context
import io.dotlottie.loader.AbstractLoader
import io.dotlottie.loader.isZipCompressed
import io.dotlottie.loader.lastSegmentName
import io.dotlottie.loader.models.DotLottie
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.gildor.coroutines.okhttp.await
import java.io.IOException

class NetworkLoader(context: Context, private val url: String): AbstractLoader(context) {

    override val fileEntryName: String
        get() = url.lastSegmentName()


    private var client: OkHttpClient = OkHttpClient()

    /**
     * set a custom OkHttpClient to passover to
     * the NetworkFetcher
     */
    fun setClient(overrideClient: OkHttpClient) {
        this.client = overrideClient
    }



    override suspend fun loadInternal(): DotLottie {
        //todo: caching, saving etc
        val request = Request
            .Builder()
            .url(url)
            .build()

        //todo: this needs significant cleanup

        client
            .newCall(request)
            .await()
            .use {response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val isZip = response.peekBody(8).byteStream().isZipCompressed()
                val inputStream = response.body?.byteStream()!! // you jackass

                val result: DotLottie? =
                    if(isZip)
                        parseZipInputStream(inputStream)
                    else
                        parseInputStream(inputStream)

                if(result!=null)
                    return result
                else
                    throw IllegalArgumentException("Could not parse file")

            }


    }


}
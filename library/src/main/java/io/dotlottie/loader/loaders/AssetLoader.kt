package io.dotlottie.loader.loaders

import android.content.Context
import io.dotlottie.loader.lastSegmentName
import java.io.InputStream

class AssetLoader(context: Context, private val assetName: String)
    : AbstractAppResourceLoader(context) {

    override val resInputStream: InputStream
        get() = context.assets.open(assetName)


    override val resEntryName: String
        get() = assetName.lastSegmentName()

}
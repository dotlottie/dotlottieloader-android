package io.dotlottie.loader.loaders

import android.content.Context
import androidx.annotation.RawRes
import java.io.InputStream

class ResLoader(context: Context, @RawRes private val resId: Int)
    : AbstractAppResourceLoader(context) {


    override val resInputStream: InputStream
        get() = context.resources.openRawResource(resId)


    override val resEntryName: String
        get() = context.resources.getResourceEntryName(resId)

}
package io.dotlottie.loader

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner
import java.io.IOException
import java.io.InputStream


internal fun String.lastSegmentName() = split("/").last()

internal fun String.withoutExt() = split(".").first()


/**
 * Determine if an [InputStream] is zip compressed or not
 */
internal fun InputStream.isZipCompressed(): Boolean {
    return try {
        for (i in ZIP_MAGIC.indices)
            if (read() != ZIP_MAGIC[i])
                return false

        close()
        true
    } catch (e: IOException) {
        false
    }
}

/**
 * obtain [LifecycleOwner] from context
 * from: https://stackoverflow.com/questions/49075413/easy-way-to-get-current-activity-fragment-lifecycleowner-from-within-view
 */
fun Context.lifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext as LifecycleOwner
    } else {
        null
    }
}
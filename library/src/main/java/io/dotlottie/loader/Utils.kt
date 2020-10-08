package io.dotlottie.loader

import java.io.IOException
import java.io.InputStream


internal fun String.lastSegmentName() = split("/").last()

internal fun String.withoutExt() = split(".").first()


/**
 * Determine if an [InputStream] is zip compressed or not
 * @param autoClose if the [InputStream] should be closed at the end
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
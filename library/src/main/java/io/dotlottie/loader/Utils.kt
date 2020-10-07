package io.dotlottie.loader

import java.io.IOException
import java.io.InputStream


/**
 * magic header for .zip files
 */
private val ZIP_MAGIC = intArrayOf(0x50, 0x4b, 0x03, 0x04)

/**
 * Determine if an inputstream is zipcompressed or not
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
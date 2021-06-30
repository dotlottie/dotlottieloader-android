package io.dotlottie.loader

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.Manifest
import io.dotlottie.loader.models.ManifestAnimation
import io.dotlottie.loader.models.manifestAdapter
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


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



val String.md5: String
    get() {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }




private fun ZipOutputStream.putByteEntry(s: String, bytes: ByteArray) {
    putNextEntry(ZipEntry(s))
    write(bytes)
    closeEntry()
}


/**
 * Returns a bytestream of this DotLottie
 * compressed into the dotLottie format.
 * If the animation already contains a manifest, it will be used,
 * otherwise the provided overrides will be used to generate a new one
 */
fun DotLottie.compress(
    loop: Boolean = true,
    themeColor: String = "#ffffff",
    speed: Float = 1.0f,
    version: Float = 1.0f,
    revision: Int = 1,
    author: String = "LottieFiles",
    generator: String = "LottieFiles dotLottieLoader-android ${BuildConfig.LIBRARY_VERSION}"
): ByteArray {

    val baos = ByteArrayOutputStream()
    val zip = ZipOutputStream(baos)

    // existing manifest, or initialize with defaults
    val man = manifest ?: Manifest(
        generator,
        version,
        revision,
        author,
        animations.map { ManifestAnimation(it.key, speed, themeColor, loop) },
        hashMapOf()
    )

    // write the manifest
    zip.putByteEntry(
        "manifest.json",
        manifestAdapter.toJson(man).toByteArray(Charsets.UTF_8)
    )


    // write the animations
    animations.forEach { zip.putByteEntry("animations/${it.key}.json", it.value) }

    //write images
    images.forEach { zip.putByteEntry("images/${it.key}", it.value) }

    // close the zip file up
    zip.close()
    return baos.toByteArray()
}
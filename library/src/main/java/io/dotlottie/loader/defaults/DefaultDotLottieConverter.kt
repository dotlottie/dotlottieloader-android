package io.dotlottie.loader.defaults

import com.squareup.moshi.JsonReader
import io.dotlottie.loader.DotLottieConverter
import io.dotlottie.loader.lastSegmentName
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.Manifest
import io.dotlottie.loader.models.manifestAdapter
import io.dotlottie.loader.withoutExt
import okio.buffer
import okio.source
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class DefaultDotLottieConverter: DotLottieConverter {


    override suspend fun parseZipInputStream(inputStream: ZipInputStream): DotLottie? {

        var manifest: Manifest? = null
        val animations: HashMap<String, ByteArray> = HashMap()
        val images: HashMap<String, ByteArray> = HashMap()

        inputStream.use {
            var entry: ZipEntry? = it.nextEntry
            while (entry!=null) {

                // parse the manifest
                val name = entry.name
                if(name.equals("manifest.json", ignoreCase = true)) {
                    manifest = manifestAdapter.fromJson(JsonReader.of(it.source().buffer()))
                } else if(name.startsWith("animations/")) {
                    animations[name.lastSegmentName().withoutExt()] = it.readBytes()
                } else if(name.startsWith("images/")) {
                    images[name.lastSegmentName()] = it.readBytes()

                // for regular zip, in a separate block here for readability and stuff
                } else if (name.contains(".json")) {
                    animations[name.lastSegmentName().withoutExt()] = it.readBytes()
                } else if (name.contains(".png") || name.contains(".webp")) {
                    images[name.lastSegmentName()] = it.readBytes()
                }

                it.closeEntry()
                entry = it.nextEntry
            }
        }

        return DotLottie(
            manifest,
            animations,
            images
        )

    }

    override suspend fun parseFileInputStream(inputStream: InputStream, entryName: String): DotLottie? {

        val manifest: Manifest? = null
        val animations: HashMap<String, ByteArray> = HashMap()
        val images: HashMap<String, ByteArray> = HashMap()

        animations[entryName.lastSegmentName().withoutExt()] = inputStream.readBytes()

        // return a blank dotLottie,
        // with only one animation

        return DotLottie(
            manifest,
            animations,
            images
        )

    }

}
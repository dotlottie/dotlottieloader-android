package io.dotlottie.loader.converters

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


    override fun parseZipInputStream(inputStream: ZipInputStream): DotLottie? {

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
                }

                //todo; handle generic bundled zip (i.e. plain .json and images)

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

    override fun parseFileInputStream(inputStream: InputStream): DotLottie? {
        TODO("Not yet implemented")
    }

}
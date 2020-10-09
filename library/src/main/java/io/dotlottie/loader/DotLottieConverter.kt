package io.dotlottie.loader

import io.dotlottie.loader.models.DotLottie
import java.io.InputStream
import java.util.zip.ZipInputStream

interface DotLottieConverter {
    /**
     * parse a zip file entry (a .lottie or .zip)
     * throw an exception on failure
     */
    fun parseZipInputStream(inputStream: ZipInputStream): DotLottie?

    /**
     * parse a file entry (a .json)
     * throw an exception on failure
     */
    fun parseFileInputStream(inputStream: InputStream, entryName: String): DotLottie?
}
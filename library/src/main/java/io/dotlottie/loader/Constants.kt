package io.dotlottie.loader

/**
 * magic header for .zip files
 */
internal val ZIP_MAGIC = intArrayOf(0x50, 0x4b, 0x03, 0x04)


/**
 * Dist cache strategies
 */
public enum class DotLottieCacheStrategy {
    NONE, MEMORY, DISK
}
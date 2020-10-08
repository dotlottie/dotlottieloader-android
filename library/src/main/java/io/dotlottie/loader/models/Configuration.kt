package io.dotlottie.loader.models

import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.defaults.DefaultDotLottieCache


enum class DotLottieCacheStrategy {
    NONE, MEMORY, DISK
}

/**
 * Model for loader configuration options
 * must initialize default values for all as
 * it will be used as the default global config
 */
data class DotLottieConfig(
    val cacheStrategy:  DotLottieCacheStrategy = DotLottieCacheStrategy.DISK,
    val cacheManager: DotLottieCache = DefaultDotLottieCache
)
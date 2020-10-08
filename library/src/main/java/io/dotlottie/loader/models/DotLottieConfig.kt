package io.dotlottie.loader.models

import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.DotLottieCacheStrategy


/**
 * Model for loader configuration options
 * must initialize default values for all as
 * it will be used as the default global config
 */
public data class DotLottieConfig(
    val cacheStrategy: DotLottieCacheStrategy? = null,
    val cacheManager: DotLottieCache? = null
)
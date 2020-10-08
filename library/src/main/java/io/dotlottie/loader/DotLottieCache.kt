package io.dotlottie.loader

import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieCacheStrategy

interface DotLottieCache {

    /**
     * fetch DotLottie from cache, with respect to the [DotLottieCacheStrategy]
     * supplied. Return null to ignore cache
     */
    suspend fun fromCache(cacheKey: String, cacheStrategy: DotLottieCacheStrategy): DotLottie?

    /**
     * enter DotLottie into cache, with respect to the [DotLottieCacheStrategy]
     * supplied.
     */
    suspend fun putCache(dotLottie: DotLottie, key: String, cacheStrategy: DotLottieCacheStrategy)

}
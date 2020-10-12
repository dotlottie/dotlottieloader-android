package io.dotlottie.loader

import android.content.Context
import io.dotlottie.loader.models.DotLottie

interface DotLottieCache {

    /**
     * if the cache requires any initialization on context, it should go here
     * might get called more than once. Must handle state yourself
     */
    fun initialize(context: Context)

    /**
     * fetch DotLottie from cache, with respect to the [DotLottieCacheStrategy]
     * supplied. Return null to ignore cache
     */
    suspend fun fromCache(cacheKey: String, cacheStrategy: DotLottieCacheStrategy): DotLottie?

    /**
     * enter DotLottie into cache, with respect to the [DotLottieCacheStrategy]
     * supplied.
     */
    suspend fun putCache(dotLottie: DotLottie, cacheKey: String, cacheStrategy: DotLottieCacheStrategy)

}
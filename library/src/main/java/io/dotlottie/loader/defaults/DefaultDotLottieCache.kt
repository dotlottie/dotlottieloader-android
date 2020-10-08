package io.dotlottie.loader.defaults

import android.util.LruCache
import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.DotLottieCacheStrategy
import io.dotlottie.loader.models.DotLottie

internal object DefaultDotLottieCache: DotLottieCache{

    val mem_cache = LruCache<String, DotLottie>(10)

    override suspend fun fromCache(
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ): DotLottie? {

        // thoughts: two level cache to avoid hitting disk?
        // or do we let diskLRUCache handle it?

        return when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> null
            DotLottieCacheStrategy.DISK,   //todo: implement (for now just memory cache)
            DotLottieCacheStrategy.MEMORY -> mem_cache.get(cacheKey)

        }
    }

    override suspend fun putCache(
        dotLottie: DotLottie,
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ) {
        when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> {
                // do nothing?
            }
            DotLottieCacheStrategy.DISK, // todo: implement (for now just memory cache)
            DotLottieCacheStrategy.MEMORY -> mem_cache.put(cacheKey, dotLottie)

        }
    }


}
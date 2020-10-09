package io.dotlottie.loader.defaults

import android.content.Context
import androidx.collection.LruCache
import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.DotLottieCacheStrategy
import io.dotlottie.loader.models.DotLottie

internal object DefaultDotLottieCache: DotLottieCache{

    val memoryCache = LruCache<String, DotLottie>(10)


    override suspend fun fromCache(
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ): DotLottie? {

        // thoughts: two level cache to avoid hitting disk?
        // or do we let diskLRUCache handle it?

        return when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> null
            DotLottieCacheStrategy.DISK, //todo: implement (just memory cache rn)
            DotLottieCacheStrategy.MEMORY -> memoryCache.get(cacheKey)
        }
    }

    override suspend fun putCache(
        dotLottie: DotLottie,
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ) {
        when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> {}
            DotLottieCacheStrategy.DISK, // todo: implement (for now just memory cache)
            DotLottieCacheStrategy.MEMORY -> memoryCache.put(cacheKey, dotLottie)
        }
    }

}
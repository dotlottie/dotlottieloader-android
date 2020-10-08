package io.dotlottie.loader.defaults

import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieCacheStrategy

internal object DefaultDotLottieCache: DotLottieCache{


    override suspend fun fromCache(
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ): DotLottie? {
        TODO("Not yet implemented")
    }

    override suspend fun putCache(
        dotLottie: DotLottie,
        key: String,
        cacheStrategy: DotLottieCacheStrategy
    ) {
        TODO("Not yet implemented")
    }


}
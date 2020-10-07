package io.dotlottie.loader.models

interface DotLottieResult {
    fun onSuccess(result: DotLottie)
    fun onError(throwable: Throwable)   //todo: pass the default back
}
package io.dotlottie.loader.models

interface DotLottieResult {
    fun onSuccess()
    fun onError(throwable: Throwable)
}
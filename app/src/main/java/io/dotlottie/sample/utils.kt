package io.dotlottie.sample

import android.util.Log
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import io.dotlottie.loader.AbstractLoader
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieResult
import java.io.ByteArrayInputStream
import java.io.InputStream

fun AbstractLoader.into(view: LottieAnimationView) {

    load(object: DotLottieResult {
        override fun onSuccess(result: DotLottie) {

            //set image handler
            view.setImageAssetDelegate(AppImageDelegate.getDelegate(view.context, result.images))

            // set the animation
            result.animations?.entries.first()?.let {
                val input = ByteArrayInputStream(it.value)
                view.setAnimation(input as InputStream, null)
                view.playAnimation()

            }
            Log.d("DotLottie", "Parsed ${result}")
        }

        override fun onError(throwable: Throwable) {
            Toast.makeText(view.context, "Error Loading Lottie", Toast.LENGTH_LONG)
                .show()

            throwable.printStackTrace()
        }

    })

}
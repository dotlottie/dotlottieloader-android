package io.dotlottie.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.dotlottie.loader.DotLottieLoader
import io.dotlottie.loader.models.DotLottieResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DotLottieLoader.with(this)
            .fromAsset("anims/something.json")
            .load(object: DotLottieResult {
                override fun onSuccess() {

                }
                override fun onError(throwable: Throwable) {

                }
            })

    }
}
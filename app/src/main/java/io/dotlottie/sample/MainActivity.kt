package io.dotlottie.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.dotlottie.loader.DotLottieLoader
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieResult
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonLoad.setOnClickListener { loadAnimation() }

    }

    private fun loadAnimation() {

        textTitle.setText(R.string.loading_anim)

        when(radiogroupSource.checkedRadioButtonId) {
            R.id.option_asset -> {
                val item = getSelectedItemAsset()
                Log.d("DotLottie", "Loading ASSET with ${item}")
                DotLottieLoader.with(this).fromAsset(item)
            }
            R.id.option_network -> {
                val item = getSelectedItemURL()
                Log.d("DotLottie", "Loading URL with ${item}")
                DotLottieLoader.with(this).fromUrl(item)
            }
            else -> {
                val item = getSelectedRawRes()
                Log.d("DotLottie", "Loading RAW with ${item}")
                DotLottieLoader.with(this).fromRaw(item)
            }
        }.load(object: DotLottieResult {
            override fun onSuccess(result: DotLottie) {
                textTitle.setText(R.string.select_option)

                //set image handler
                animationView.setImageAssetDelegate(AppImageDelegate.getDelegate(this@MainActivity, result.images))

                // set the animation
                result.animations?.entries.first()?.let {
                    val input = ByteArrayInputStream(it.value)
                    animationView.setAnimation(input as InputStream, null)
                    animationView.playAnimation()

                }
                Log.d("DotLottie", "Parsed ${result}")

            }

            override fun onError(throwable: Throwable) {
                textTitle.setText(R.string.error_loading)
                Toast.makeText(this@MainActivity, R.string.error_loading, Toast.LENGTH_LONG).show()
                throwable.printStackTrace()
            }
        })
    }

    private fun getSelectedItemURL() = when (radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> "https://dotlottie.io/sample_files/animation-external-image.lottie"
        R.id.anim_internal -> "https://dotlottie.io/sample_files/animation-inline-image.lottie"
        else -> "https://dotlottie.io/sample_files/animation.lottie"
    }

    private fun getSelectedRawRes() = when (radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> R.raw.animation_external_image
        R.id.anim_internal -> R.raw.animation_inline_image
        else -> R.raw.animation
    }

    private fun getSelectedItemAsset() = when (radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> "animation_external_image.lottie"
        R.id.anim_internal -> "animation_inline_image.lottie"
        else -> "animation.lottie"
    }

}
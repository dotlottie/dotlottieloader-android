package io.dotlottie.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.dotlottie.loader.DotLottieLoader
import io.dotlottie.loader.models.DotLottieResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonLoad.setOnClickListener { loadAnimation() }

    }

    private fun loadAnimation() {

        textTitle.setText(R.string.loading_anim)

        when(radiogroupAnimationType.checkedRadioButtonId) {
            R.id.option_asset -> DotLottieLoader.with(this).fromAsset(getSelectedItemAsset())
            R.id.option_network -> DotLottieLoader.with(this).fromUrl(getSelectedItemURL())
            else -> DotLottieLoader.with(this).fromRaw(getSelectedRawRes())
        }.load(object: DotLottieResult {
            override fun onSuccess() {
                textTitle.setText(R.string.select_option)
            }

            override fun onError(throwable: Throwable) {
                textTitle.setText(R.string.error_loading)
                Toast.makeText(this@MainActivity, R.string.error_loading, Toast.LENGTH_LONG).show()
                throwable.printStackTrace()
            }
        })
    }

    private fun getSelectedItemURL() = when (radiogroupSource.checkedRadioButtonId){
        R.id.anim_external -> "https://dotlottie.io/sample_files/animation-external-image.lottie"
        R.id.anim_internal -> "https://dotlottie.io/sample_files/animation-inline-image.lottie"
        else -> "https://dotlottie.io/sample_files/animation.lottie"
    }

    private fun getSelectedRawRes() = when (radiogroupSource.checkedRadioButtonId){
        R.id.anim_external -> R.raw.animation_external_image
        R.id.anim_internal -> R.raw.animation_inline_image
        else -> R.raw.animation
    }

    private fun getSelectedItemAsset() = when (radiogroupSource.checkedRadioButtonId){
        R.id.anim_external -> "animation_external_image.lottie"
        R.id.anim_internal -> "animation_inline_image.lottie"
        else -> "animation.lottie"
    }

}
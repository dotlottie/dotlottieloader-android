package io.dotlottie.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import io.dotlottie.loader.DotLottieLoader
import io.dotlottie.loader.compress
import io.dotlottie.loader.models.DotLottie
import io.dotlottie.loader.models.DotLottieResult
import io.dotlottie.sample.databinding.ActivityMainBinding
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentAnim: DotLottie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonLoad.setOnClickListener { loadAnimation() }
        binding.buttonShare.setOnClickListener { onShare() }

    }

    private fun loadAnimation() {

        binding.textTitle.setText(R.string.loading_anim)

        when(binding.radiogroupSource.checkedRadioButtonId) {
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

                currentAnim = result
                binding.textTitle.setText(R.string.select_option)

                //set image handler
                binding.animationView.setImageAssetDelegate(AppImageDelegate.getDelegate(this@MainActivity, result.images))

                // set the animation
                result.animations?.entries.first()?.let {
                    val input = ByteArrayInputStream(it.value)
                    binding.animationView.setAnimation(input as InputStream, null)
                    binding.animationView.playAnimation()

                }
                Log.d("DotLottie", "Parsed ${result}")

            }

            override fun onError(throwable: Throwable) {
                binding.textTitle.setText(R.string.error_loading)
                Toast.makeText(this@MainActivity, R.string.error_loading, Toast.LENGTH_LONG).show()
                throwable.printStackTrace()
            }
        })
    }

    private fun onShare() {
        currentAnim?.let {

            // export the current anim to a .lottie
            val f = File.createTempFile("share",".lottie", cacheDir)
            f.writeBytes(it.compress(
                true,
                "#ffffff",
                1.0f,
                1.0f,
                1,
                "OverrideAuthor",
                "Override Generator"
            ))



            // now share it?
            val uri = FileProvider.getUriForFile(this, "io.dotlottie.sample.provider", f)
            ShareCompat.IntentBuilder(this)
                .setType("application/x-zip")
                .setSubject("Sharing dotLottie")
                .setStream(uri)
                .setChooserTitle("Share dotLottie")
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .also { startActivity(it) }

        }
    }

    private fun getSelectedItemURL() = when (binding.radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> "https://dotlottie.io/sample_files/animation-external-image.lottie"
        R.id.anim_internal -> "https://dotlottie.io/sample_files/animation-inline-image.lottie"
        R.id.anim_json -> "https://assets9.lottiefiles.com/packages/lf20_z01atlv1.json"
        else -> "https://dotlottie.io/sample_files/animation.lottie"
    }


    private fun getSelectedRawRes() = when (binding.radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> R.raw.animation_external_image
        R.id.anim_internal -> R.raw.animation_inline_image
        R.id.anim_json -> R.raw.simplejson
        else -> R.raw.animation
    }

    private fun getSelectedItemAsset() = when (binding.radiogroupAnimationType.checkedRadioButtonId){
        R.id.anim_external -> "animation_external_image.lottie"
        R.id.anim_internal -> "animation_inline_image.lottie"
        R.id.anim_json -> "simplejson.json"
        else -> "animation.lottie"
    }

}
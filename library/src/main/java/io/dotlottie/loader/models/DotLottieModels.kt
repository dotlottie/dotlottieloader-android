package io.dotlottie.loader.models

/**
 * The manifest for a dotLottie
 * provides information about the animation, and
 * describes the contents of the bundle
 */
data class Manifest(
    val generator: String,
    val version: Float,
    val revision: Int,
    val author: String,
    val animations: List<ManifestAnimation>,
    val custom: HashMap<String, Any>?
)

/**
 * Animation spec as declared in the [Manifest].
 * All dotLotties MUST contain at least one
 */
data class ManifestAnimation(
    val id: String,
    val speed: Float = 1.0f,
    val themeColor: String?,
    val loop: Boolean = false
)
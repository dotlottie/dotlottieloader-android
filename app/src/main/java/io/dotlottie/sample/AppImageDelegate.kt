package io.dotlottie.sample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.LruCache
import com.airbnb.lottie.ImageAssetDelegate


object AppImageDelegate {

    val interal_cache = LruCache<String, Bitmap>(10)

    fun getDelegate(context: Context, images: Map<String, ByteArray>) = //url: String,
        ImageAssetDelegate {

            val filename: String = it.fileName
            val opts = BitmapFactory.Options().apply {
                inScaled = true
                inDensity = 160
            }

            if (filename.startsWith("data:") && filename.indexOf("base64,") > 0) { // Contents look like a base64 data URI, with the format data:image/png;base64,<data>.
                val data: ByteArray = try {
                    Base64.decode(
                        filename.substring(filename.indexOf(',') + 1),
                        Base64.DEFAULT
                    )
                } catch (e: IllegalArgumentException) {
                    return@ImageAssetDelegate null
                }
                return@ImageAssetDelegate BitmapFactory.decodeByteArray(data, 0, data.size, opts)
            } else if(images.containsKey(filename)) {

                images[filename]?.let {
                    return@ImageAssetDelegate BitmapFactory.decodeByteArray(it, 0, it.size, opts)
                }

                return@ImageAssetDelegate null

            } else {

                //this block to load from url?

//                try{
//                    val uri = Uri.parse(url)
//                    val imgUrl = url.replace(uri.lastPathSegment?:"", "${it.dirName}${it.fileName}")
//
//                    interal_cache.get(imgUrl)?.let {
//                        return@ImageAssetDelegate it
//                    }
//
////                    //else
////                    Glide.with(context)
////                        .load(imgUrl)
////                        .into(object: CustomTarget<Drawable>(it.width, it.height) {
////                            override fun onLoadCleared(placeholder: Drawable?) {}
////                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
////                                interal_cache.put(imgUrl, resource.toBitmap())
////                            }
////
////                        })
//
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
                return@ImageAssetDelegate null
            }
        }


}

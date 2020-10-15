package io.dotlottie.loader.defaults

import android.content.Context
import androidx.collection.LruCache
import com.jakewharton.disklrucache.DiskLruCache
import io.dotlottie.loader.BuildConfig
import io.dotlottie.loader.DotLottieCache
import io.dotlottie.loader.DotLottieCacheStrategy
import io.dotlottie.loader.md5
import io.dotlottie.loader.models.DotLottie
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

internal object DefaultDotLottieCache: DotLottieCache{

    private val memoryCache = LruCache<String, DotLottie>(10)
    private var diskCache: DiskLruCache? = null


    override fun initialize(context: Context) {
        if(diskCache==null || diskCache?.isClosed==true) {

            try {
                val dir = File(context.cacheDir, "dotLottieCache")

                if (!dir.exists())
                    dir.mkdirs()

                diskCache = DiskLruCache.open(
                    dir,
                    BuildConfig.LIBRARY_VERSION,
                    1,
                    1024 * 1024 * 20 //20mb
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    override suspend fun fromCache(
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ): DotLottie? {

        // thoughts: two level cache to avoid hitting disk?
        // or do we let diskLRUCache handle it?
        val diskkey = cacheKey.md5

        return when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> null
            DotLottieCacheStrategy.MEMORY -> memoryCache.get(cacheKey)
            DotLottieCacheStrategy.DISK -> {

                //hit
//                memoryCache.get(cacheKey)?.let { return it }

                //miss
                diskCache?.get(diskkey)?.let {

                    (ObjectInputStream(it.getInputStream(0))
                        ?.readObject() as DotLottie)
                        ?.let {
//                            memoryCache.put(cacheKey, it)
                            return it
                        }

                }
            }
        }
    }

    override suspend fun putCache(
        dotLottie: DotLottie,
        cacheKey: String,
        cacheStrategy: DotLottieCacheStrategy
    ) {

        val diskkey = cacheKey.md5

        when(cacheStrategy) {
            DotLottieCacheStrategy.NONE -> {}
            DotLottieCacheStrategy.MEMORY -> memoryCache.put(cacheKey, dotLottie)
            DotLottieCacheStrategy.DISK -> {
                //layer 1
//                memoryCache.put(cacheKey, dotLottie)

                // flush it
                diskCache?.get(diskkey)?.let { diskCache?.remove(diskkey) }

                //to disk
                diskCache?.edit(diskkey)?.let { editor ->

                    val oos = ObjectOutputStream(editor.newOutputStream(0))
                    oos.writeObject(dotLottie)
                    oos.flush()
                    editor.commit()
                }

            }

        }
    }

}
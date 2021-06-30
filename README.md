# dotLottieLoader

[![](https://jitpack.io/v/dotlottie/dotlottieloader-android.svg)](https://jitpack.io/#dotlottie/dotlottieloader-android)


## Introducing dotLottie

<p align="center">
  <img src="assets/dotLottie2048-1024.png" width="400">
</p>

dotLottie is an open-source file format that aggregates one or more Lottie files and their associated resources into a single file. 
They are ZIP archives compressed with the Deflate compression method and carry the file extension of ".lottie".

### dotLottieLoader

dotLottieLoader is a library to help downloading and deflating a .lottie file, giving access to the animation,
as well as the assets included in the bundle

## View documentation, FAQ, help, examples, and more at [dotlottie.io](http://dotlottie.io/)

## Example

To run the example project, clone the repo, and the sample app

## Installation

Install via Jitpack

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

```gradle
dependencies {
    ...
    implementation 'com.github.dotlottie:dotlottieloader-android:{version}'
}
```

[![](https://jitpack.io/v/dotlottie/dotlottieloader-android.svg)](https://jitpack.io/#dotlottie/dotlottieloader-android)

## Using dotLottie

#### loading from network
```kotlin
DotLottieLoader.with(context)
    .fromUrl(lottieUrl)
    .load(object: DotLottieResult {
        override fun onSuccess(result: DotLottie) {
            // handle success
        }

        override fun onError(throwable: Throwable) {
            // handle errors
        }
    })
```

#### loading from app assets

Load from raw resource
```kotlin
DotLottieLoader.with(context).fromRaw(rawResID).load(...)
```

Load from assets
```kotlin
DotLottieLoader.with(context).fromAsset(assetName).load(...)
```

#### Using the DotLottie

```kotlin
data class DotLottie(
    val manifest: Manifest?,
    val animations: Map<String, ByteArray>,
    val images: Map<String, ByteArray>
): Serializable

data class Manifest(
    val generator: String,
    val version: Float,
    val revision: Int?,
    val author: String,
    val animations: List<ManifestAnimation>,
    val custom: Map<String, Any>?
): Serializable

data class ManifestAnimation(
    val id: String,
    val speed: Float = 1.0f,
    val themeColor: String?,
    val loop: Boolean = false
): Serializable
```

for example, using it with lottie-android:

```kotlin
DotLottieLoader.with(this)
    .fromUrl(item)
    .load(object: DotLottieResult {
        override fun onSuccess(result: DotLottie) {

            //set image handler. Check example app for 
            // sample source
            animationView.setImageAssetDelegate(
                AppImageDelegate.getDelegate(
                    this@MainActivity, 
                    result.images
                )
            )
            
            // set the animation
            result.animations?.entries.first()?.let {
                val input = ByteArrayInputStream(it.value)
                
                // set animation from input stream. Pass non-null
                // cachekey to let LottieAnimationView cache the
                // animation internally as well
                animationView.setAnimation(input as InputStream, null)
                animationView.playAnimation()
            }
        }

        override fun onError(throwable: Throwable) {
            Toast.makeText(this@MainActivity, R.string.error_loading, Toast.LENGTH_LONG).show()
            throwable.printStackTrace()
        }
    })
```

#### Exporting dotLottie

We can export a `DotLottie` object to disk using the .compress() method.
If the original animation provided a manifest, it will be retain, otherwise
a new manifest with defaults (or provided defaults) will be generated

```
    val f = File(cacheDir, "example.lottie")
    val exported = myDotLottie.compress(loop = true, themeColor = "#dedede")
    f.writeBytes(exported)

    ...
```


## Configurations

The library provides some configuration options to customize its behavior

#### Global configurations

```kotlin
       DotLottieLoader.setConfig(DotLottieConfig(
            cacheStrategy,  //optional
            cacheManager,   //optional
            converter       //optional
        ))
```

* `cacheStrategy` - the caching strategy to use, `DotLottieCacheStrategy.NONE`, `DotLottieCacheStrategy.MEMORY`, `DotLottieCacheStrategy.DISK`. Defaults to `DISK`
* `cacheManager` - Responsible for handling the cache. Provide your own subclass of `DotLottieCache` to override the default. The default cache manager uses `LRUCache` for in memory, and `DiskLRUCache` for disk caching.
* `DotLottieConverter` - Responsible for parsing incoming files and converting them to `DotLottie` objects. Provide custom implementations if needed


#### Per request configurations

You may also pass a `DotLottieConfig` object per request by using

```kotlin
DotLottieLoader.with(context)
    .fromUrl(lottieUrl)
    .withConfig(DotLottieConfig(
        ...
    ))
```

When requesting from the network, an additional configuration is exposed to override the OkHttpClient used for the request

```kotlin
DotLottieLoader.with(this)
    .fromUrl(lottieUrl)
    .withClient(customOkHttpClient)
    .withConfig(
        DotLottieConfig(
        ...
    ))
```

## Author

[Naail Abdul Rahman](https://github.com/kudanai) | kudanai@gmail.com


# License
```text
Copyright (c) 2020 LottieFiles

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software 
is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all 
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
package space.lianxin.base.di

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import space.lianxin.base.net.imageloader.BaseImageLoaderStrategy
import space.lianxin.base.net.imageloader.ImageLoader
import space.lianxin.base.net.imageloader.glide.GlideImageLoaderStrategy

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 13:50
 * ===========================================
 */

const val KODEIN_MODULE_IMAGELOADER_TAG = "kodein_module_imageloader_tag"

val imageLoaderModule = Kodein.Module(KODEIN_MODULE_IMAGELOADER_TAG) {

  bind<BaseImageLoaderStrategy>() with singleton {
    GlideImageLoaderStrategy()
  }

  bind<ImageLoader>() with singleton { ImageLoader(instance()) }

}
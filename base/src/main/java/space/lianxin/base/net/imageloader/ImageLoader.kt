package space.lianxin.base.net.imageloader

import org.kodein.di.generic.instance
import space.lianxin.base.app.BaseApplication

/**
 * ===========================================
 * 处理图片请求
 *
 * @author: lianxin
 * @date: 2020/12/15 14:06
 * ===========================================
 */
class ImageLoader constructor(private var strategy: BaseImageLoaderStrategy) {

    fun loadImage(config: ImageConfig) {
        strategy.loadImage(config)
    }

    fun setLoadImgStrategy(strategy: BaseImageLoaderStrategy) {
        this.strategy = strategy
    }

}

/** 图片加载的dsl */
fun imageLoad(config: ImageConfig.Builder.() -> Unit) {
    val loader: ImageLoader by BaseApplication.INSTANCE.kodein.instance()
    val builder = ImageConfig.builder()
    builder.apply(config)
    loader.loadImage(builder.build())
}
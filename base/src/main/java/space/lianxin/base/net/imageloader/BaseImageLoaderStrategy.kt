package space.lianxin.base.net.imageloader

import android.content.Context
import java.io.File

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 14:04
 * ===========================================
 */
interface BaseImageLoaderStrategy {

    /** 加载图片 */
    fun loadImage(config: ImageConfig)

    /** 获取缓存文件 */
    fun getCacheFile(context: Context, url: String): File?

    /** 清除内存缓存 */
    fun clearMemory(context: Context)

    /** 清除磁盘缓存 */
    fun cleanDiskCache(context: Context)
}
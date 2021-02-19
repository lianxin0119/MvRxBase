package space.lianxin.base.net.imageloader.glide.transformations

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import java.security.MessageDigest


/**
 * Glide图片Bitmap转换的基类.
 * Create by YangYang on 2018/10/12 10:16.
 */
abstract class BitmapTransformation : Transformation<Bitmap> {

    abstract fun key(): String

    override fun transform(
        context: Context, resource: Resource<Bitmap>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap> {
        if (!Util.isValidDimensions(outWidth, outHeight)) {
            throw IllegalArgumentException(
                "Cannot apply transformation on width: " + outWidth + " or height: " + outHeight
                        + " less than or equal to zero and not Target.SIZE_ORIGINAL"
            )
        }
        val bitmapPool = Glide.get(context).bitmapPool
        val toTransform = resource.get()
        val targetWidth = if (outWidth == Target.SIZE_ORIGINAL) toTransform.width else outWidth
        val targetHeight = if (outHeight == Target.SIZE_ORIGINAL) toTransform.height else outHeight
        val transformed = transform(
            context.applicationContext, bitmapPool, toTransform,
            targetWidth, targetHeight
        )

        val result: Resource<Bitmap>?
        result = if (toTransform == transformed) {
            resource
        } else {
            BitmapResource.obtain(transformed, bitmapPool)
        }
        return result!!
    }

    /** 获取一个缩放到给定宽高比的图片 */
    protected fun zoomBitmap(
        pool: BitmapPool,
        bitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val bitmapRate = width.toFloat() / height.toFloat()
        val whRate = outWidth.toFloat() / outHeight.toFloat()
        if (bitmapRate == whRate) {
            return bitmap
        }
        val newWidth: Float
        val newHeight: Float
        if (bitmapRate > whRate) {
            newWidth = height.toFloat() * whRate
            newHeight = height.toFloat()
        } else {
            newWidth = width.toFloat()
            newHeight = width.toFloat() / whRate
        }
        val x: Float = if (width > newWidth) {
            (width - newWidth) / 2.0f
        } else {
            (newWidth - width) / 2.0f
        }
        val y: Float = if (height > newHeight) {
            (height - newHeight) / 2.0f
        } else {
            (newHeight - height) / 2.0f
        }

        val newBitmap: Bitmap =
            pool.get(newWidth.toInt(), newHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        val src = Rect(x.toInt(), y.toInt(), (newWidth + x).toInt(), (newHeight + y).toInt())
        val dst = RectF(0f, 0f, newWidth, newHeight)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return newBitmap
    }

    protected abstract fun transform(
        context: Context, pool: BitmapPool,
        toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(key().toByteArray())
    }
}
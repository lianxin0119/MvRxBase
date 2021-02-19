package space.lianxin.base.net.imageloader.glide.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * 高斯模糊处理.
 * Create by YangYang on 2018/10/12 10:18.
 */
class BlurTransformation @JvmOverloads constructor(
    private val radius: Int = MAX_RADIUS,
    private val sampling: Int = DEFAULT_DOWN_SAMPLING
) : BitmapTransformation() {

    override fun key(): String {
        return "BlurTransformation(radius=$radius, sampling=$sampling)"
    }

    override fun transform(
        context: Context, pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        val zoomBitmap = zoomBitmap(pool, toTransform, outWidth, outHeight)

        val width = zoomBitmap.width
        val height = zoomBitmap.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling

        var bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(zoomBitmap, 0f, 0f, paint)

        bitmap = doBlur(context, bitmap, radius)
        return bitmap
    }

    @Throws(RSRuntimeException::class)
    private fun doBlur(context: Context, bitmap: Bitmap, radius: Int): Bitmap {
        var rs: RenderScript? = null
        var input: Allocation? = null
        var output: Allocation? = null
        var blur: ScriptIntrinsicBlur? = null
        try {
            rs = RenderScript.create(context)
            rs!!.messageHandler = RenderScript.RSMessageHandler()
            input = Allocation.createFromBitmap(
                rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
            output = Allocation.createTyped(rs, input!!.type)
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            blur!!.setInput(input)
            blur.setRadius(radius.toFloat())
            blur.forEach(output)
            output!!.copyTo(bitmap)
        } finally {
            rs?.destroy()
            input?.destroy()
            output?.destroy()
            blur?.destroy()
        }
        return bitmap
    }

    companion object {
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }

}
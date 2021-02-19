package space.lianxin.base.net.imageloader.glide

/**
 * Glide加载图片的进度回调.
 * Create by YangYang on 2019/5/25 11:47.
 */
interface OnImageProgressListener {

    fun onProgress(
        url: String,
        isComplete: Boolean,
        percentage: Int,
        bytesRead: Long,
        totalBytes: Long
    )

}
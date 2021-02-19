package space.lianxin.base.net.imageloader.cache

import com.bumptech.glide.load.Key

import java.security.MessageDigest

/**
 * Glide缓存的key.
 * Create by YangYang on 2019/5/25 11:41.
 */
class DataCacheKey(val sourceKey: Key, private val signature: Key) : Key {

    override fun toString(): String {
        return "DataCacheKey{sourceKey=$sourceKey, signature=$signature}"
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        sourceKey.updateDiskCacheKey(messageDigest)
        signature.updateDiskCacheKey(messageDigest)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataCacheKey

        if (sourceKey != other.sourceKey) return false
        if (signature != other.signature) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceKey.hashCode()
        result = 31 * result + signature.hashCode()
        return result
    }


}
package space.lianxin.base.net.imageloader.cache

import com.bumptech.glide.load.Key
import com.bumptech.glide.util.LruCache
import com.bumptech.glide.util.Util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 *  Glide缓存的key生成类.
 * Create by YangYang on 2019/5/25 11:41.
 */
class SafeKeyGenerator {
    private val loadIdToSafeHash = LruCache<Key, String>(1000)

    fun getSafeKey(key: Key): String? {
        var safeKey: String?
        synchronized(loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key)
        }
        if (safeKey == null) {
            try {
                val messageDigest = MessageDigest.getInstance("SHA-256")
                key.updateDiskCacheKey(messageDigest)
                safeKey = Util.sha256BytesToHex(messageDigest.digest())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            synchronized(loadIdToSafeHash) {
                loadIdToSafeHash.put(key, safeKey)
            }
        }
        return safeKey
    }
}

package space.lianxin.base.net.imageloader.glide

import android.annotation.SuppressLint
import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate

/**
 * SSLSocketClient
 * Create by YangYang on 2018/10/11 19:02.
 */
object SSLSocketClient {

    val sslSocketFactory: SSLSocketFactory
        get() {
            try {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustManager, SecureRandom())
                return sslContext.socketFactory
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }

    private val trustManager: Array<TrustManager>
        get() = arrayOf(object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
    val hostnameVerifier: HostnameVerifier
        get() = HostnameVerifier { _, _ -> true }
}

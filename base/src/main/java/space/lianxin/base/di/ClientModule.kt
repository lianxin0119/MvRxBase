package space.lianxin.base.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import space.lianxin.base.net.http.converter.AbnormalConverterFactory
import space.lianxin.base.net.http.ssl.SSLManager
import java.io.File
import java.util.concurrent.TimeUnit

const val KODEIN_MODULE_CLIENT_TAG = "kodein_module_client_tag"
const val KODEIN_TAG_FILE_RXCACHE = "kodein_tag_file_rxcache"

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 16:35
 * ===========================================
 */
object ClientModule {

    var callBack: ((build: OkHttpClient.Builder) -> Unit)? = null
    var httpDebug: Boolean = false
    private const val TIME_OUT = 30L

    val clientModule = Kodein.Module(KODEIN_MODULE_CLIENT_TAG) {

        bind<GsonBuilder>() with singleton {
            GsonBuilder().serializeNulls()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(
                    Double::class.java,
                    JsonSerializer<Double> { src, _, _ ->
                        if (src != null && src % 1 != 0.0) {
                            // 解决科学计数法转换的问题
                            return@JsonSerializer JsonPrimitive(src.toLong())
                        }
                        JsonPrimitive(src)
                    })
        }

        bind<OkHttpClient.Builder>() with singleton { OkHttpClient.Builder() }

        bind<Retrofit.Builder>() with singleton { Retrofit.Builder() }

        bind<File>(KODEIN_TAG_FILE_RXCACHE) with singleton {
            val cacheDirectory = File(instance<File>(KODEIN_TAG_FILE_CACHEDIR), "RxCache")
            if (!cacheDirectory.exists()) {
                cacheDirectory.mkdirs()
            }
            cacheDirectory
        }

        bind<RxCache>() with singleton {
            RxCache.Builder()
                .persistence(instance(KODEIN_TAG_FILE_RXCACHE), GsonSpeaker())
        }

        bind<OkHttpClient>() with singleton {
            val builder = instance<OkHttpClient.Builder>()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            val interceptors = instance<List<Interceptor>>()
            builder.apply {
                interceptors.forEach {
                    addInterceptor(it)
                }
                if (httpDebug) { //log拦截
                    addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    ) //log
                    // 测试服忽略证书校验
                    sslSocketFactory(SSLManager.createSSLSocketFactory())
                    hostnameVerifier { _, _ -> true }
                }
                callBack?.invoke(this)
            }.build()
        }

        bind<Retrofit>() with singleton {
            instance<Retrofit.Builder>()
                .baseUrl(instance<HttpUrl>())
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //使用rxjava
                .addConverterFactory(AbnormalConverterFactory.create()) //使用自定义的解析
                .build()
        }
    }
}
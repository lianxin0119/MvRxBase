package space.lianxin.base.di

import android.os.Environment
import androidx.annotation.Size
import okhttp3.HttpUrl
import okhttp3.Interceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import space.lianxin.base.app.BaseApplication
import space.lianxin.base.net.http.GlobeHttpHandler
import java.io.File
import java.util.*

const val KODEIN_MODULE_GLOBECINFIG_TAG = "kodein_module_globecinfig_tag"
const val KODEIN_TAG_FILE_CACHEDIR = "kodein_tag_file_cachedir"

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 13:50
 * ===========================================
 */
class GlobeConfigModule private constructor(builder: Builder) {

    private lateinit var mApiUrl: HttpUrl
    private lateinit var mHandler: GlobeHttpHandler
    private lateinit var mInterceptors: List<Interceptor>
    private lateinit var mCacheFile: File
    val globeConfigModule = Kodein.Module(KODEIN_MODULE_GLOBECINFIG_TAG) {

        bind<HttpUrl>() with singleton {
            mApiUrl
        }

        bind<List<Interceptor>>() with singleton { mInterceptors }

        bind<GlobeHttpHandler>() with singleton { mHandler }

        bind<File>(KODEIN_TAG_FILE_CACHEDIR) with singleton {
            mCacheFile
        }
    }

    init {
        this.mApiUrl = builder.baseUrl ?: throw IllegalArgumentException("baseUrl can not be empty")
        this.mHandler = builder.handler ?: GlobeHttpHandler.EMPTY
        this.mInterceptors = builder.interceptors
        this.mCacheFile = builder.cacheFile
            ?: if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                // 获取系统管理的sd卡缓存文件
                var file: File? = BaseApplication.INSTANCE.externalCacheDir
                // 如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                if (file == null) {
                    val cacheFilePath = BaseApplication.INSTANCE.getExternalFilesDir(null)?.path
                    file = File(cacheFilePath.orEmpty())
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                }
                file
            } else {
                BaseApplication.INSTANCE.cacheDir
            }
    }

    class Builder {

        var baseUrl: HttpUrl? = null
        var handler: GlobeHttpHandler? = null
        val interceptors = ArrayList<Interceptor>()
        var cacheFile: File? = null

        fun baseUrl(@Size(min = 1) baseUrl: String): Builder {
            if (baseUrl.isBlank()) {
                throw IllegalArgumentException("baseUrl can not be empty")
            }
            this.baseUrl = HttpUrl.parse(baseUrl)
            return this
        }

        /** 用来处理http响应结果 */
        fun globeHttpHandler(handler: GlobeHttpHandler): Builder {
            this.handler = handler
            return this
        }

        /** 动态添加任意个interceptor */
        fun addInterceptor(interceptor: Interceptor): Builder {
            this.interceptors.add(interceptor)
            return this
        }


        fun cacheFile(cacheFile: File): Builder {
            this.cacheFile = cacheFile
            return this
        }

        fun build(): GlobeConfigModule {
            return GlobeConfigModule(this)
        }

    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

}
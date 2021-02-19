package space.lianxin.base.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.android.support.androidSupportModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import space.lianxin.base.di.ClientModule
import space.lianxin.base.di.GlobeConfigModule
import space.lianxin.base.di.appModule
import space.lianxin.base.di.imageLoaderModule
import space.lianxin.base.integration.*
import java.util.*

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 11:58
 * ===========================================
 */
open class BaseApplication : Application(), KodeinAware {

    private val mAppLifecycleList = ArrayList<AppLifecycle>()
    private val mActivityLifecycleCallbacksList = ArrayList<ActivityLifecycleCallbacks>()
    private var configModules: List<ConfigModule>? = null

    private var launcherTime = 0L

    open val appManager: AppManager by instance()
    private val repositoryManager: IRepositoryManager by instance()

    // 初始化全局kodein
    override val kodein: Kodein = Kodein.lazy {
        bind<Context>() with singleton { this@BaseApplication }
        import(androidCoreModule(this@BaseApplication))
        import(androidSupportModule(this@BaseApplication))

        import(imageLoaderModule)
        configModules = getModuleConfig()
        configModules?.let {
            import(getGlobeConfigModule(it).globeConfigModule)
        }
        import(appModule)
        import(ClientModule.clientModule)
        initKodein(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        mAppLifecycleList.forEach {
            it.attachBaseContext(this)
        }
        MultiDex.install(this)
        launcherTime = System.currentTimeMillis()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        //MMKV初始化
        MMKV.initialize(this)
        if (isMainProcess()) {
            registerActivityLifecycleCallbacks(ActivityLifecycle(appManager))
            injectConfigModule(configModules)
            initThirdPart()
            mAppLifecycleList.forEach {
                it.onCreate(this)
            }
            mActivityLifecycleCallbacksList.forEach {
                registerActivityLifecycleCallbacks(it)
            }
            initInMainProcess()
        }
    }

    /** 程序终止的时候执行 */
    override fun onTerminate() {
        super.onTerminate()
        mAppLifecycleList.forEach {
            it.onTerminate(this)
        }
    }

    /** 获取模块的配置 */
    private fun getModuleConfig(): List<ConfigModule> = ManifestParser(this).parse()

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明[ConfigModule]的实现类,和Glide的配置方式相似
     */
    private fun getGlobeConfigModule(modules: List<ConfigModule>): GlobeConfigModule {
        val builder = GlobeConfigModule
            .builder()
            .baseUrl("https://api.github.com") //为了防止用户没有通过GlobeConfigModule配置baseurl,而导致报错,所以提前配置个默认baseurl
        for (module in modules) {
            module.applyOptions(this, builder)
        }
        return builder.build()
    }

    /** 初始化第三方的一些东西 */
    private fun initThirdPart() {
        Utils.init(this) //Utils
        LogUtils.getConfig().apply {
            isLogSwitch = BuildConfig.DEBUG //log开关
            globalTag = "MvRxBase" //全局标签
            stackDeep = 3 //log栈
        }
    }

    private fun injectConfigModule(modules: List<ConfigModule>?) {
        modules?.forEach {
            it.injectAppLifecycle(this, mAppLifecycleList)
            it.registerComponents(this, repositoryManager)
            it.injectActivityLifecycle(this, mActivityLifecycleCallbacksList)
        }
    }

    /** 是否主进程 */
    private fun isMainProcess(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am.runningAppProcesses == null) {
            return false
        }
        val processInfo = am.runningAppProcesses
        val mainProcessName = packageName
        val myPid = Process.myPid()
        for (info in processInfo) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    /** 初始化kodein */
    protected open fun initKodein(builder: Kodein.MainBuilder) {}

    /** 在主进程中进行操作 */
    protected open fun initInMainProcess() {}

    companion object {
        lateinit var INSTANCE: BaseApplication
    }

}
package space.lianxin.base.integration

import android.app.Application
import android.content.Context
import space.lianxin.base.app.AppLifecycle
import space.lianxin.base.di.GlobeConfigModule

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 12:15
 * ===========================================
 */
interface ConfigModule {

    /** 使用[GlobeConfigModule.Builder]给框架配置一些配置参数 */
    fun applyOptions(context: Context, builder: GlobeConfigModule.Builder)

    /** 使用[IRepositoryManager]给框架注入一些网络请求和数据缓存等服务 */
    fun registerComponents(context: Context, repositoryManager: IRepositoryManager)

    /** 使用[AppLifecycle]在Application的声明周期中注入一些操作 */
    fun injectAppLifecycle(context: Context, lifecycles: ArrayList<AppLifecycle>)

    /** 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作 */
    fun injectActivityLifecycle(
        context: Context,
        lifecycles: ArrayList<Application.ActivityLifecycleCallbacks>
    )
}
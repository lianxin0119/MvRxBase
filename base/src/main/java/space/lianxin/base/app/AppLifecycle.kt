package space.lianxin.base.app

import android.app.Application
import android.content.Context

/**
 * ===========================================
 * 用于代理 [Application] 的生命周期。
 *
 * @author: lianxin
 * @date: 2020/12/15 11:55
 * ===========================================
 */
interface AppLifecycle {

    fun attachBaseContext(context: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application?)

}
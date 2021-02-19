package space.lianxin.base.di

import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import space.lianxin.base.app.BaseApplication
import space.lianxin.base.integration.AppManager
import space.lianxin.base.integration.IRepositoryManager
import space.lianxin.base.integration.RepositoryManager

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 14:17
 * ===========================================
 */

const val KODEIN_MODULE_APP_TAG = "kodein_module_app_tag"

val appModule = Kodein.Module(KODEIN_MODULE_APP_TAG) {

    // AppManager
    bind<AppManager>() with singleton { AppManager(BaseApplication.INSTANCE) }

    // IRepositoryManager
    bind<IRepositoryManager>() with singleton { RepositoryManager(instance(), instance()) }

    // GSON
    bind<Gson>() with singleton { Gson() }

}
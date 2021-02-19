package space.lianxin.base.integration

import android.app.Application
import android.content.pm.PackageManager
import java.util.*

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 13:44
 * ===========================================
 */
class ManifestParser constructor(private var context: Application) {

    private val moduleValue = "ConfigModule"

    fun parse(): List<ConfigModule> {
        val modules = ArrayList<ConfigModule>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (moduleValue == appInfo.metaData.get(key)) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }

        return modules
    }

    private fun parseModule(className: String): ConfigModule {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
        }

        val module: Any
        try {
            module = clazz.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException(
                "Unable to instantiate ConfigModule implementation for $clazz",
                e
            )
        } catch (e: IllegalAccessException) {
            throw RuntimeException(
                "Unable to instantiate ConfigModule implementation for $clazz",
                e
            )
        }

        if (module !is ConfigModule) {
            throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
        }
        return module
    }

}
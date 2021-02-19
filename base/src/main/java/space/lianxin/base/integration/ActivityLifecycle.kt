package space.lianxin.base.integration

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import space.lianxin.base.ui.activity.BaseActivity

/**
 * ===========================================
 * 把Activity的放入AppManager中。
 * @author: lianxin
 * @date: 2020/12/15 13:39
 * ===========================================
 */
class ActivityLifecycle constructor(
    private val appManager: AppManager
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activity?.let {
            appManager.addActivity(it)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity?) {
        activity?.let {
            if (it is BaseActivity<out ViewBinding>) appManager.setCurrentActivity(it)
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity?) {
        activity?.let {
            appManager.removeActivity(it)
        }
    }

}
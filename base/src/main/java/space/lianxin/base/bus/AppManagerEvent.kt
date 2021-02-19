package space.lianxin.base.bus

/**
 * app级别的事件.
 * Create by YangYang on 2018/10/11 10:42.
 */
sealed class AppManagerEvent {

    /** 打开指定的activity可以是class也可以是Intent */
    data class StartActivity(val page: Any) : AppManagerEvent()

    /** 关闭所有页面退出不会杀死进程 */
    object KillAllActivity : AppManagerEvent()

    /** 杀死进程退出 */
    object ExitApp : AppManagerEvent()

}
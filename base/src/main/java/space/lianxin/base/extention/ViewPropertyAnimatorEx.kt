package space.lianxin.base.extention

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.view.ViewPropertyAnimator

/**
 * ===========================================
 * 动画相关扩展。
 *
 * @author: lianxin
 * @date: 2020/12/18 20:42
 * ===========================================
 */

/** 开始监听 */
inline fun ViewPropertyAnimator.onStart(crossinline func: () -> Unit): ViewPropertyAnimator {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)
            func()
        }
    })
    return this
}

/** 结束监听 */
inline fun ViewPropertyAnimator.onEnd(crossinline func: () -> Unit): ViewPropertyAnimator {
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            func()
        }
    })
    return this
}

/** 延时 */
fun ViewPropertyAnimator.delay(delay: Long): ViewPropertyAnimator {
    startDelay = delay
    return this
}

/** 差值器 */
fun ViewPropertyAnimator.interpolate(interp: TimeInterpolator): ViewPropertyAnimator {
    interpolator = interp
    return this
}
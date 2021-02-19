package space.lianxin.base.extention

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindUntilEvent
import java.util.concurrent.TimeUnit

/**
 * ===========================================
 * View相关扩展。
 *
 * @author: lianxin
 * @date: 2020/12/18 19:41
 * ===========================================
 */

/** 点击事件（600毫秒内不可重复点击） */
@SuppressLint("CheckResult")
inline fun View.click(crossinline function: () -> Unit) {
    RxView.clicks(this)
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .subscribe {
            function()
        }
}

/**
 * 点击事件（600毫秒内不可重复点击）
 * @param event 执行到当前生命周期不响应事件
 */
@SuppressLint("CheckResult")
inline fun View.click(
    owner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    crossinline function: () -> Unit
) {
    RxView.clicks(this)
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .bindUntilEvent(owner, event)
        .subscribe {
            function()
        }
}

/**
 * 长按事件
 */
@SuppressLint("CheckResult")
inline fun View.longClick(crossinline function: () -> Unit) {
    RxView.longClicks(this)
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .subscribe {
            function()
        }
}

/**
 * 长按事件
 * @param event 执行到当前生命周期不响应事件
 */
@SuppressLint("CheckResult")
inline fun View.longClick(
    owner: LifecycleOwner,
    event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    crossinline function: () -> Unit
) {
    RxView.longClicks(this)
        .throttleFirst(600, TimeUnit.MILLISECONDS)
        .bindUntilEvent(owner, event)
        .subscribe {
            function()
        }
}

/** 隐藏View（在屏幕中不占位） */
fun View.gone() {
    this.visibility = View.GONE
}

/** 隐藏View（在屏幕中占位） */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/** 显示View */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/** 设置View可用 */
fun View.enable() {
    this.isEnabled = true
}

/** 设置View不可用 */
fun View.disable() {
    this.isEnabled = false
}

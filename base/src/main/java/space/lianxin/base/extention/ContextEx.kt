package space.lianxin.base.extention

import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

/**
 * ===========================================
 * Context相关的扩展.
 *
 * @author: lianxin
 * @date: 2020/12/17 13:24
 * ===========================================
 */

fun Context.getClipboardManager() =
    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun Context.getConnectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.getInputMethodManager() =
    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.getResColor(resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.getResDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun Context.inflate(
    layoutResource: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(this).inflate(layoutResource, parent, attachToRoot)
}

fun Context.screenWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    val display = windowManager.defaultDisplay
    display.getMetrics(dm)
    return dm.widthPixels
}

fun Context.screenHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    val display = windowManager.defaultDisplay
    display.getMetrics(dm)
    return dm.heightPixels
}
package space.lianxin.base.ui.holder

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * ===========================================
 * Holder的基类.
 *
 * @author: lianxin
 * @date: 2020/12/17 11:55
 * ===========================================
 */
abstract class BaseEpoxyHolder : EpoxyHolder() {

    lateinit var view: View

    override fun bindView(itemView: View) {
        view = itemView
    }

    protected fun <V : View> bind(id: Int): ReadOnlyProperty<BaseEpoxyHolder, V> =
        Lazy { holder: BaseEpoxyHolder, prop ->
            holder.view.findViewById(id) as V?
                ?: throw IllegalStateException("View ID $id for '${prop.name}' not found.")
        }

    /** 代理 */
    private class Lazy<V>(private val initializer: (BaseEpoxyHolder, KProperty<*>) -> V) :
        ReadOnlyProperty<BaseEpoxyHolder, V> {

        private var value: Any? = EMPTY
        override fun getValue(thisRef: BaseEpoxyHolder, property: KProperty<*>): V {
            if (value == EMPTY) {
                value = initializer(thisRef, property)
            }
            @Suppress("UNCHECKED_CAST")
            return value as V
        }

        private object EMPTY
    }
}
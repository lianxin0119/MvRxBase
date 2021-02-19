package space.lianxin.base.mvvm

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.MvRxStateStore

/**
 * ===========================================
 * MvRxViewModel
 *
 * @author: lianxin
 * @date: 2020/12/18 20:51
 * ===========================================
 */
abstract class MvRxViewModel<S : MvRxState>(
    initialState: S,
    private val store: MvRxStateStore<S> = CustomMvRxStateStore(initialState)
) : BaseMvRxViewModel<S>(
    initialState,
    debugMode = BuildConfig.DEBUG,
    stateStore = store
) {

    protected var stateErrorHandler: ((e: Throwable) -> Unit)? = null
        set(value) {
            field = value
            (store as? CustomMvRxStateStore)?.errorHandleCallback = value
        }

}
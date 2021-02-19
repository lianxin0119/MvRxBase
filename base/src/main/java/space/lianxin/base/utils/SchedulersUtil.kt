package space.lianxin.base.utils

import io.reactivex.FlowableTransformer
import io.reactivex.MaybeTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Rx线程切换工具类.
 * Create by YangYang on 2018/10/10 17:42.
 */
object SchedulersUtil {

    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { flowable ->
            flowable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
        return MaybeTransformer { flowable ->
            flowable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> observeSchedulersIO(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.observeOn(Schedulers.io())
        }
    }

    fun <T> observeSchedulersMain(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.observeOn(AndroidSchedulers.mainThread())
        }
    }
}
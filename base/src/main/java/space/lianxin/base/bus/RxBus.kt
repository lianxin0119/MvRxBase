package space.lianxin.base.bus

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import space.lianxin.base.utils.SchedulersUtil

/**
 * Create by YangYang on 2018/10/10 16:46.
 */
class RxBus private constructor() {

    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    fun post(o: Any) {
        bus.onNext(o)
    }

    fun <T> toFlowable(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType)
    }

    fun <T> toFlowableMainThread(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).compose<T>(SchedulersUtil.applyFlowableSchedulers())
    }

    fun <T> toFlowableBackpressure(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).onBackpressureBuffer()
    }

    fun <T> toFlowableMainThreadBackpressure(eventType: Class<T>): Flowable<T> {
        return bus.ofType(eventType).onBackpressureBuffer()
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
    }

    fun <T> toFlowable(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .subscribe(consumer)
    }

    fun <T> toFlowableMainThread(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
            .subscribe(consumer)
    }

    fun <T> toFlowableBackpressure(eventType: Class<T>, consumer: Consumer<T>): Disposable {
        return bus.ofType(eventType)
            .onBackpressureBuffer()
            .subscribe(consumer)
    }

    fun <T> toFlowableMainThreadBackpressure(
        eventType: Class<T>,
        consumer: Consumer<T>
    ): Disposable {
        return bus.ofType(eventType)
            .onBackpressureBuffer()
            .compose<T>(SchedulersUtil.applyFlowableSchedulers())
            .subscribe(consumer)
    }

    private object Holder {
        val INSTANCE = RxBus()
    }

    companion object {
        fun getInstance() = Holder.INSTANCE
    }
}
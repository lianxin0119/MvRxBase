package space.lianxin.base.repository

import io.rx_cache2.internal.RxCache
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import space.lianxin.base.app.BaseApplication
import space.lianxin.base.integration.IRepositoryManager

/**
 * ===========================================
 * 本地缓存的数据源基类.
 *
 * @author: lianxin
 * @date: 2021/3/3 13:07
 * ===========================================
 */
open class BaseLocalDataSource : ILocalDataSource {

    protected var kodein: Kodein = BaseApplication.INSTANCE.kodein
    private val repositoryManager: IRepositoryManager by kodein.instance()

    /** RxCache网络请求缓存 */
    protected val rxCache: RxCache by kodein.instance()

    /** 获取RxCache的Service */
    fun <T> cacheService(service: Class<T>): T =
        repositoryManager.obtainCacheService(service)

}
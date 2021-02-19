package space.lianxin.base.integration

/**
 * ===========================================
 * @author: lianxin
 * @date: 2020/12/15 13:33
 * ===========================================
 */
interface IRepositoryManager {

    /** 注入RetrofitService */
    fun injectRetrofitService(vararg services: Class<*>)

    /** 注入CacheService */
    fun injectCacheService(vararg services: Class<*>)

    /** 根据传入的Class获取对应的Retrofit service */
    fun <T> obtainRetrofitService(service: Class<T>): T

    /** 根据传入的Class获取对应的RxCache service */
    fun <T> obtainCacheService(cache: Class<T>): T
}
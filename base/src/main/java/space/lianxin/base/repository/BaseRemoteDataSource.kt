package space.lianxin.base.repository

import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import retrofit2.Retrofit
import space.lianxin.base.app.BaseApplication
import space.lianxin.base.integration.IRepositoryManager

/**
 * ===========================================
 * 远程数据源的基类.
 *
 * @author: lianxin
 * @date: 2021/3/3 13:09
 * ===========================================
 */
open class BaseRemoteDataSource : IRemoteDataSource {

    protected var kodein: Kodein = BaseApplication.INSTANCE.kodein
    private val repositoryManager: IRepositoryManager by kodein.instance()

    protected val retrofit: Retrofit by kodein.instance()

    fun <T> retrofitService(service: Class<T>): T =
        repositoryManager.obtainRetrofitService(service)

}
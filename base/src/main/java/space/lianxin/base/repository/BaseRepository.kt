package space.lianxin.base.repository

/**
 * ===========================================
 * Repository基类.
 *
 * @author: lianxin
 * @date: 2021/3/3 13:15
 * ===========================================
 */
open class BaseRepository<T : IRemoteDataSource, R : ILocalDataSource>(
    val remoteDataSource: T,
    val localDataSource: R
) : IRepository

open class BaseRepositoryLocal<T : ILocalDataSource>(
    val remoteDataSource: T
) : IRepository

open class BaseRepositoryRemote<T : IRemoteDataSource>(
    val remoteDataSource: T
) : IRepository

open class BaseRepositoryNothing() : IRepository
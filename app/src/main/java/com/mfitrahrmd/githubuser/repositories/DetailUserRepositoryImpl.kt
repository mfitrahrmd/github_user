package com.mfitrahrmd.githubuser.repositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toDBDetailUser
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.DetailUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.UserFollowersDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.UserFollowingDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserFollowersRemoteMediator
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserFollowingRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class DetailUserRepositoryImpl(
    private val _dataSource: DataSource,
    private val _detailUserDao: DetailUserDao,
    private val _userFollowingDao: UserFollowingDao,
    private val _userFollowersDao: UserFollowersDao,
    private val _favoriteUserDao: FavoriteUserDao
) : DetailUserRepository {
    override suspend fun getFollowing(username: String): Flow<PagingData<User>> {
        delay(1_000L)
        return withContext(Dispatchers.IO) {
            val user = _detailUserDao.findOneByUsername(username)
        if (user == null) {
            throw Exception("user not found")
        }
            return@withContext Pager(
                config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
                pagingSourceFactory = { _userFollowingDao.findManyByUserDbId(user.dbId) },
                remoteMediator = UserFollowingRemoteMediator(
                    username,
                    _dataSource,
                    _userFollowingDao,
                    _detailUserDao
                )
            ).flow.map {
                it.map { dbUF ->
                    withContext(Dispatchers.IO) {
                        val favorite = _favoriteUserDao.findOneByDbId(dbUF.dbId)
                        dbUF.toUser(User.Favorite(favorite != null, favorite?.addedAt))
                    }
                }
            }
        }
    }

    override suspend fun getFollowers(username: String): Flow<PagingData<User>> {
        delay(1_000L)
        return withContext(Dispatchers.IO) {
        val user = _detailUserDao.findOneByUsername(username)
        if (user == null) {
            throw Exception("user not found")
        }

        return@withContext Pager(
            config = PagingConfig(
                pageSize = SearchUsersRepositoryImpl.DEFAULT_PAGE_SIZE,
                maxSize = SearchUsersRepositoryImpl.DEFAULT_MAX_SIZE
            ),
            pagingSourceFactory = { _userFollowersDao.findManyByUserDbId(user.dbId) },
            remoteMediator = UserFollowersRemoteMediator(
                username,
                _dataSource,
                _userFollowersDao,
                _detailUserDao
            )
        ).flow.map {
            it.map { dbUF ->
                withContext(Dispatchers.IO) {
                    val favorite = _favoriteUserDao.findOneByDbId(dbUF.dbId)
                    dbUF.toUser(User.Favorite(favorite != null, favorite?.addedAt))
                }
            }
        }
        }
    }

    override suspend fun get(username: String): Flow<Result<User?>> {
        return flow<Result<User?>> {
            try {
                val sourceUser = _dataSource.findUserByUsername(username)
                if (sourceUser != null) {
                    _detailUserDao.deleteOneByUsername(username)
                    _detailUserDao.insertOne(sourceUser.toDBDetailUser())
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
            val dbUser = _detailUserDao.findOneByUsername(username)
            if (dbUser == null) {
                throw Exception("user not found")
            }
            val user = coroutineScope {
                async {
                    return@async withContext(Dispatchers.IO) {
                        val favorite = _favoriteUserDao.findOneByDbId(dbUser.dbId)
                        dbUser.toUser(User.Favorite(favorite != null, favorite?.addedAt))
                    }
                }
            }
            emit(Result.Success(user.await()))
        }.onStart { emit(Result.Loading()) }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
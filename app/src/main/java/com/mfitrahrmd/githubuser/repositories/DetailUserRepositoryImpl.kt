package com.mfitrahrmd.githubuser.repositories

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
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserFollowersRemoteMediator
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserFollowingRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class DetailUserRepositoryImpl(
    private val _User_dataSource: UserDataSource,
    private val _detailUserDao: DetailUserDao,
    private val _userFollowingDao: UserFollowingDao,
    private val _userFollowersDao: UserFollowersDao,
    private val _favoriteUserDao: FavoriteUserDao
) : DetailUserRepository {
    override suspend fun getFollowing(username: String): Flow<PagingData<User>> {
        return withContext(Dispatchers.IO) {
            val user = _detailUserDao.findOneByUsername(username)
            if (user == null) {
                throw Exception("user not found")
            }
            return@withContext Pager(
                config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
                pagingSourceFactory = { _userFollowingDao.findManyByUserDbIdWithFavorite(user.dbId) },
                remoteMediator = UserFollowingRemoteMediator(
                    username,
                    _User_dataSource,
                    _userFollowingDao,
                    _detailUserDao
                )
            ).flow.map {
                it.map { e ->
                    e.userFollowing.toUser(
                        User.Favorite(
                            e.favoriteUser != null,
                            e.favoriteUser?.addedAt
                        )
                    )
                }
            }
        }
    }

    override suspend fun getFollowers(username: String): Flow<PagingData<User>> {
        return withContext(Dispatchers.IO) {
            val user = _detailUserDao.findOneByUsername(username)
            if (user == null) {
                throw Exception("user not found")
            }

            return@withContext Pager(
                config = PagingConfig(
                    pageSize = DEFAULT_PAGE_SIZE,
                    maxSize = DEFAULT_MAX_SIZE
                ),
                pagingSourceFactory = { _userFollowersDao.findManyByUserDbIdWithFavorite(user.dbId) },
                remoteMediator = UserFollowersRemoteMediator(
                    username,
                    _User_dataSource,
                    _userFollowersDao,
                    _detailUserDao
                )
            ).flow.map {
                it.map { e ->
                    e.userFollowers.toUser(
                        User.Favorite(
                            e.favoriteUser != null,
                            e.favoriteUser?.addedAt
                        )
                    )
                }
            }
        }
    }

    override fun get(username: String): Flow<Result<User?>> {
        return flow<Result<User?>> {
            try {
                val sourceUser = _User_dataSource.findUserByUsername(username)
                if (sourceUser != null) {
                    _detailUserDao.replace(sourceUser.toDBDetailUser())
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
            val dbUser = _detailUserDao.findOneByUsername(username)
            if (dbUser == null) {
                emit(Result.Error("user not found"))
                return@flow
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
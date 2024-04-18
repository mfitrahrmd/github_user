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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class DetailUserRepositoryImpl(
    private val _userDataSource: UserDataSource,
    private val _detailUserDao: DetailUserDao,
    private val _userFollowingDao: UserFollowingDao,
    private val _userFollowersDao: UserFollowersDao,
    private val _favoriteUserDao: FavoriteUserDao
) : DetailUserRepository {
    override suspend fun getFollowing(username: String): Flow<PagingData<User>> {
        return withContext(Dispatchers.IO) {
            delay(500L)
            val user = _detailUserDao.findOneByUsername(username)
            if (user == null) {
                throw Exception("user not found")
            }
            return@withContext Pager(
                config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
                pagingSourceFactory = { _userFollowingDao.findManyByUserDbIdWithFavorite(user.dbId) },
                remoteMediator = UserFollowingRemoteMediator(
                    username,
                    _userDataSource,
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
            delay(500L)
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
                    _userDataSource,
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

    override suspend fun get(username: String): Flow<Result<User?>> {
        return flow<Result<User?>> {
            try {
                val sourceUser = _userDataSource.findUserByUsername(username)
                if (sourceUser != null) {
                    _detailUserDao.deleteOneByUsername(sourceUser.login)
                    _detailUserDao.insertOne(sourceUser.toDBDetailUser())
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
            val dbUser = _detailUserDao.findOneByUsernameWithFavorite(username)
            dbUser.collect {
                if (it.detailUser == null) {
                    emit(Result.Error("user not found"))
                } else {
                    emit(
                        Result.Success(
                            it.detailUser.toUser(
                                User.Favorite(
                                    it.favoriteUser != null,
                                    it.favoriteUser?.addedAt
                                )
                            )
                        )
                    )
                }
            }
        }.onStart { emit(Result.Loading()) }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
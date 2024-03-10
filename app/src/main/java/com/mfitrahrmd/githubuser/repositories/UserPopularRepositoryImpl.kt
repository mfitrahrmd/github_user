package com.mfitrahrmd.githubuser.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.local.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.local.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.remote.services.UserService
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserPopularRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserPopularRepositoryImpl(
    private val _userService: UserService,
    private val _popularUserDao: PopularUserDao,
    private val _favoriteUserDao: FavoriteUserDao,
) : UserPopularRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun get(
        location: String
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _popularUserDao.findAll() },
            remoteMediator = UserPopularRemoteMediator(location, _userService, _popularUserDao)
        ).flow.map {
            it.map { dbPU ->
                withContext(Dispatchers.IO) {
                    val favorite = _favoriteUserDao.findOneById(dbPU.id)
                    dbPU.toUser(User.Favorite(favorite != null, favorite?.addedAt))
                }
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
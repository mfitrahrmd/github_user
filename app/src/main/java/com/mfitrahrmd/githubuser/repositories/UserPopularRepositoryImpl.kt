package com.mfitrahrmd.githubuser.repositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.services.UserService
import com.mfitrahrmd.githubuser.repositories.remotemediator.PopularUserRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserPopularRepositoryImpl(
    private val _dataSource: DataSource,
    private val _popularUserDao: PopularUserDao,
    private val _favoriteUserDao: FavoriteUserDao,
) : UserPopularRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun get(
        location: String
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _popularUserDao.findAllWithFavorite() },
            remoteMediator = PopularUserRemoteMediator(location, _dataSource, _popularUserDao)
        ).flow.map {
            it.map { dbPU ->
                dbPU.popularUser.toUser(User.Favorite(dbPU.favoriteUser != null, dbPU.favoriteUser?.addedAt))
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
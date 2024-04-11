package com.mfitrahrmd.githubuser.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource
import com.mfitrahrmd.githubuser.repositories.remotemediator.PopularUsersRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PopularUsersRepositoryImpl(
    private val _User_dataSource: UserDataSource,
    private val _popularUserDao: PopularUserDao,
) : PopularUsersRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun get(
        location: String
    ): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _popularUserDao.findAllWithFavorite() },
            remoteMediator = PopularUsersRemoteMediator(location, _User_dataSource, _popularUserDao)
        ).flow.map {
            it.map { e ->
                e.popularUser.toUser(User.Favorite(e.favoriteUser != null, e.favoriteUser?.addedAt))
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
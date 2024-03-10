package com.mfitrahrmd.githubuser.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.local.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.local.dao.RemoteKeyDao
import com.mfitrahrmd.githubuser.repositories.local.dao.SearchUserDao
import com.mfitrahrmd.githubuser.repositories.remote.RemoteService
import com.mfitrahrmd.githubuser.repositories.remotemediator.UserSearchRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class UserSearchRepositoryImpl(
    private val _remoteService: RemoteService,
    private val _searchUserDao: SearchUserDao,
    private val _favoriteUserDao: FavoriteUserDao,
    private val _remoteKeyDao: RemoteKeyDao
) : UserSearchRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun get(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _searchUserDao.findAll() },
            remoteMediator = UserSearchRemoteMediator(
                query,
                _remoteService,
                _searchUserDao,
                _remoteKeyDao
            )
        ).flow.map {
            it.map { dbSU ->
                withContext(Dispatchers.IO) {
                    val favorite = _favoriteUserDao.findOneById(dbSU.id)
                    dbSU.toUser(User.Favorite(favorite != null, favorite?.addedAt))
                }
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_MAX_SIZE = 50
    }
}
package com.mfitrahrmd.githubuser.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.FavoriteUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.RemoteKeyDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.SearchUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.remotemediator.SearchUserRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


@OptIn(ExperimentalPagingApi::class)
class SearchUsersRepositoryImpl(
    private val _dataSource: DataSource,
    private val _searchUserDao: SearchUserDao,
    private val _favoriteUserDao: FavoriteUserDao,
    private val _remoteKeyDao: RemoteKeyDao
) : SearchUsersRepository {
    override fun get(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, maxSize = DEFAULT_MAX_SIZE),
            pagingSourceFactory = { _searchUserDao.findAll() },
            remoteMediator = SearchUserRemoteMediator(
                query,
                _dataSource,
                _searchUserDao,
                _remoteKeyDao
            )
        ).flow.map {
            it.map { dbSU ->
                withContext(Dispatchers.IO) {
                    val favorite = _favoriteUserDao.findOneByDbId(dbSU.dbId)
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
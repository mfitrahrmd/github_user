package com.mfitrahrmd.githubuser.repositories.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.entities.db.DBSearchUserWithFavorite
import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.mapper.toDBSearchUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.SearchUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class SearchUserRemoteMediator(
    private val query: String,
    private val _dataSource: DataSource,
    private val _searchUserDao: SearchUserDao,
) : RemoteMediator<Int, DBSearchUserWithFavorite>() {
    private var _nextPage: Int? = null

    private suspend fun fetch(page: Int, pageSize: Int): List<RemoteUser> {
        return _dataSource.searchUsers(query, page, pageSize)
    }

    private suspend fun cleanLocalData() {
        withContext(Dispatchers.IO) {
            _searchUserDao.deleteAll()
        }
    }

    private suspend fun upsertLocalData(localEntities: List<DBSearchUser>) {
        withContext(Dispatchers.IO) {
            _searchUserDao.insertMany(localEntities)
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DBSearchUserWithFavorite>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                STARTING_PAGE_INDEX
            }

            LoadType.APPEND -> {
                _nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = _nextPage != null)
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        try {
            val items = fetch(page, state.config.pageSize)
            val end = items.isEmpty() || items.size < state.config.pageSize
            _nextPage = if (loadType == LoadType.REFRESH) 2
            else if (end) null
            else _nextPage?.plus(1)
            withContext(Dispatchers.IO) {
                if (loadType == LoadType.REFRESH) {
                    cleanLocalData()
                }
                upsertLocalData(items.toDBSearchUser())
            }

            return MediatorResult.Success(endOfPaginationReached = end)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

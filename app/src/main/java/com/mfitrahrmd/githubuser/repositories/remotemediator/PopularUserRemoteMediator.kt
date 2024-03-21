package com.mfitrahrmd.githubuser.repositories.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.db.DBPopularUserWithFavorite
import com.mfitrahrmd.githubuser.entities.network.SourceUser
import com.mfitrahrmd.githubuser.mapper.toDBPopularUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PopularUserRemoteMediator(
    private val _location: String,
    private val _dataSource: DataSource,
    private val _popularUserDao: PopularUserDao
) : RemoteMediator<Int, DBPopularUserWithFavorite>() {
    private var _nextPage: Int? = null

    private suspend fun fetch(page: Int, pageSize: Int): List<SourceUser> {
        return _dataSource.searchUsers("location:$_location", page, pageSize)
    }

    private suspend fun cleanLocalData() {
        withContext(Dispatchers.IO) {
            _popularUserDao.deleteAll()
        }
    }

    private fun toLocalEntity(networkEntity: SourceUser): DBPopularUser {
        return networkEntity.toDBPopularUser()
    }

    private fun upsertLocalData(localEntities: List<DBPopularUser>) {
        _popularUserDao.insertMany(localEntities)
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DBPopularUserWithFavorite>
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
                upsertLocalData(items.map {
                    toLocalEntity(it)
                })
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
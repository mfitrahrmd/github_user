package com.mfitrahrmd.githubuser.repositories.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBSearchUser
import com.mfitrahrmd.githubuser.entities.db.RemoteKey
import com.mfitrahrmd.githubuser.mapper.toDBSearchUser
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.RemoteKeyDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.SearchUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.RemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class SearchUserRemoteMediator(
    private val query: String,
    private val _dataSource: DataSource,
    private val _searchUserDao: SearchUserDao,
    private val _remoteKeyDao: RemoteKeyDao
) : RemoteMediator<Int, DBSearchUser>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBSearchUser>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                val nextKey = remoteKey?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
        try {
            val users = _dataSource.searchUsers(
                query,
                page,
                state.config.pageSize
            )
            if (loadType == LoadType.REFRESH) {
                withContext(Dispatchers.IO) {
                    _searchUserDao.deleteAll()
                    _remoteKeyDao.deleteAll()
                }
            }
            val end = users.isEmpty() || users.size < state.config.pageSize
            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (end) null else page + 1
            val keys = users.map {
                RemoteKey(it.id, prevKey, nextKey)
            }
            withContext(Dispatchers.IO) {
                _remoteKeyDao.insertMany(keys)
                _searchUserDao.insertMany(users.toUser().toDBSearchUser())
            }

            return MediatorResult.Success(endOfPaginationReached = end)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, DBSearchUser>): RemoteKey? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { dbSU ->
            withContext(Dispatchers.IO) {
                _remoteKeyDao.findByUserId(dbSU.id)
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, DBSearchUser>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                _remoteKeyDao.findByUserId(userId)
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

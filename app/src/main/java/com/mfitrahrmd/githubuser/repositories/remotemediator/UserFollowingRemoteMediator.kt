package com.mfitrahrmd.githubuser.repositories.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowing
import com.mfitrahrmd.githubuser.entities.db.DBUserFollowingWithFavorite
import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.mapper.toDBUserFollowing
import com.mfitrahrmd.githubuser.repositories.cache.dao.DetailUserDao
import com.mfitrahrmd.githubuser.repositories.cache.dao.UserFollowingDao
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class UserFollowingRemoteMediator(
    private val _username: String,
    private val _dataSource: DataSource,
    private val _userFollowingDao: UserFollowingDao,
    private val _detailUserDao: DetailUserDao
) : RemoteMediator<Int, DBUserFollowingWithFavorite>() {
    private var _nextPage: Int? = null

    private suspend fun fetch(page: Int, pageSize: Int): List<RemoteUser> {
        return _dataSource.listUserFollowing(_username, page, pageSize)
    }

    private suspend fun cleanLocalData() {
        val user = _detailUserDao.findOneByUsername(_username)
        if (user != null) {
            withContext(Dispatchers.IO) {
                _userFollowingDao.deleteManyByUserDbId(user.dbId)
            }
        }
    }

    private suspend fun upsertLocalData(localEntities: List<DBUserFollowing>) {
        withContext(Dispatchers.IO) {
            _userFollowingDao.insertMany(localEntities)
        }
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, DBUserFollowingWithFavorite>
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
                val user = _detailUserDao.findOneByUsername(_username)
                if (user != null) {
                    upsertLocalData(items.map { ru ->
                        ru.toDBUserFollowing(user.dbId)
                    })
                }
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
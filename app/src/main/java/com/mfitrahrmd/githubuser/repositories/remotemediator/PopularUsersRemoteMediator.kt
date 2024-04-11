package com.mfitrahrmd.githubuser.repositories.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.entities.db.DBPopularUser
import com.mfitrahrmd.githubuser.entities.db.DBPopularUserWithFavorite
import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.mapper.toDBPopularUser
import com.mfitrahrmd.githubuser.repositories.cache.dao.PopularUserDao
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PopularUsersRemoteMediator(
    private val _location: String,
    private val _User_dataSource: UserDataSource,
    private val _popularUserDao: PopularUserDao
) : RemoteMediator<Int, DBPopularUserWithFavorite>() {
    private var _nextPage: Int? = null

    private suspend fun fetch(page: Int, pageSize: Int): List<RemoteUser> {
        val popularUsers = _User_dataSource.searchUsers("location:$_location", page, pageSize)
        val popularUsersDetail = withContext(Dispatchers.IO) {
            popularUsers.map {
                async {
                    _User_dataSource.findUserByUsername(it.login)
                }
            }
        }.awaitAll().filterNotNull()

        return popularUsersDetail
    }

    private suspend fun cleanLocalData() {
        withContext(Dispatchers.IO) {
            _popularUserDao.deleteAll()
        }
    }

    private suspend fun upsertLocalData(localEntities: List<DBPopularUser>) {
        withContext(Dispatchers.IO) {
            _popularUserDao.insertMany(localEntities)
        }
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
                upsertLocalData(items.toDBPopularUser())
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
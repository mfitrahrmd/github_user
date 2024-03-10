package com.mfitrahrmd.githubuser.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.mapper.NetworkToLocal
import com.mfitrahrmd.githubuser.repositories.local.BatchUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<LocalEntity : Any, NetworkEntity : NetworkToLocal<LocalEntity>>(
    private val _batchUpdate: BatchUpdate<LocalEntity>, // local data source
    private val _fetch: suspend (page: Int, pageSize: Int) -> List<NetworkEntity> // network data source where to get the latest data
) : RemoteMediator<Int, LocalEntity>() {
    private var _nextPage: Int? = null

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                _nextPage = 2
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
            val items = _fetch(page, state.config.pageSize)
            val end = items.isEmpty() || items.size < state.config.pageSize
            _nextPage = if (end) null else _nextPage?.plus(1)
            CoroutineScope(Dispatchers.IO).launch {
                if (loadType == LoadType.REFRESH) {
                    _batchUpdate.deleteAll()
                }
                _batchUpdate.insertMany(items.map { it.toLocalEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = end)
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

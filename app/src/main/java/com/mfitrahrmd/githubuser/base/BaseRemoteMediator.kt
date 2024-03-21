package com.mfitrahrmd.githubuser.base

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mfitrahrmd.githubuser.repositories.cache.BatchOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<LocalEntity : Any, NetworkEntity>(
    private val _batchOperation: BatchOperation<LocalEntity>, // local data source
) : RemoteMediator<Int, LocalEntity>() {
    private var _nextPage: Int? = null

    // network data source where to get the latest data
    abstract suspend fun fetch(page: Int, pageSize: Int): List<NetworkEntity>

    // how local data source clean the data when loading type is "refresh"
    abstract suspend fun cleanLocalData()

    // how to upsert to local data
    abstract suspend fun upsertLocalData(localEntities: List<LocalEntity>)

    abstract suspend fun toLocalEntity(networkEntity: NetworkEntity): LocalEntity

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocalEntity>
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
            _nextPage = if (loadType == LoadType.REFRESH) 2 else if (end) null else _nextPage?.plus(1)
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
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}

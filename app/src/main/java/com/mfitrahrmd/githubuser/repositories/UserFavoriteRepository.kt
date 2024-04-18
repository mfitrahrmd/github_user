package com.mfitrahrmd.githubuser.repositories

import androidx.paging.PagingData
import com.mfitrahrmd.githubuser.entities.User
import kotlinx.coroutines.flow.Flow

interface UserFavoriteRepository {
    suspend fun add(user: User)
    suspend fun remove(user: User)
    fun get(): Flow<PagingData<User>>
    fun getAll(): List<User>
}
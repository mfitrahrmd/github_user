package com.mfitrahrmd.githubuser.repositories

import androidx.paging.PagingData
import com.mfitrahrmd.githubuser.entities.User
import kotlinx.coroutines.flow.Flow

interface DetailUserRepository {
    suspend fun getFollowing(username: String): Flow<PagingData<User>>
    suspend fun getFollowers(username: String): Flow<PagingData<User>>
    fun get(username: String): Flow<Result<User?>>
}
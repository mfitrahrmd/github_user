package com.mfitrahrmd.githubuser.repositories

import androidx.paging.PagingData
import com.mfitrahrmd.githubuser.entities.User
import kotlinx.coroutines.flow.Flow

interface SearchUsersRepository {
    fun get(query: String): Flow<PagingData<User>>
}
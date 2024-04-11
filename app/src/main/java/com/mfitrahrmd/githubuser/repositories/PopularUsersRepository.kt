package com.mfitrahrmd.githubuser.repositories

import androidx.paging.PagingData
import com.mfitrahrmd.githubuser.entities.User
import kotlinx.coroutines.flow.Flow

interface PopularUsersRepository {
    fun get(location: String): Flow<PagingData<User>>
}
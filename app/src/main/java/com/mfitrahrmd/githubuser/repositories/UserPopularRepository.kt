package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User

interface UserPopularRepository {
    suspend fun searchPopularUsers(location: String, page: String? = null): WithPagination<List<User>>
}
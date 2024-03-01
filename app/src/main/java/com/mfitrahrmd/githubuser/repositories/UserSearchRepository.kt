package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User

interface UserSearchRepository {
    suspend fun searchUsers(query: String, page: String? = null): WithPagination<List<User>>
}
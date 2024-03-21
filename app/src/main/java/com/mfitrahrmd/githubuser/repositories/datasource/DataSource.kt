package com.mfitrahrmd.githubuser.repositories.datasource

import com.mfitrahrmd.githubuser.entities.network.SourceUser

interface DataSource {
    suspend fun searchUsers(query: String, page: Int? = null, perPage: Int? = null): List<SourceUser>
    suspend fun findUserByUsername(username: String): SourceUser?
    suspend fun listUserFollowers(username: String, page: Int? = null, perPage: Int? = null): List<SourceUser>
    suspend fun listUserFollowing(username: String, page: Int? = null, perPage: Int? = null): List<SourceUser>
}
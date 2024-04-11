package com.mfitrahrmd.githubuser.repositories.datasource

import com.mfitrahrmd.githubuser.entities.remote.RemoteUser

interface UserDataSource {
    suspend fun searchUsers(
        query: String,
        page: Int? = null,
        perPage: Int? = null
    ): List<RemoteUser>

    suspend fun findUserByUsername(username: String): RemoteUser?
    suspend fun listUserFollowers(
        username: String,
        page: Int? = null,
        perPage: Int? = null
    ): List<RemoteUser>

    suspend fun listUserFollowing(
        username: String,
        page: Int? = null,
        perPage: Int? = null
    ): List<RemoteUser>
}
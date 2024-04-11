package com.mfitrahrmd.githubuser.repositories.datasource.remote

import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource

class RemoteUserDataSource private constructor(
    private val _remoteService: RemoteService
) : UserDataSource {
    override suspend fun searchUsers(
        query: String,
        page: Int?,
        perPage: Int?
    ): List<RemoteUser> {
        val res = _remoteService.userApi.searchUsers(query, page.toString(), perPage.toString())
        val body = res.body()

        return body?.items ?: emptyList()
    }

    override suspend fun findUserByUsername(username: String): RemoteUser? {
        val res = _remoteService.userApi.findUserByUsername(username)
        val body = res.body()

        return body
    }

    override suspend fun listUserFollowers(username: String, page: Int?, perPage: Int?): List<RemoteUser> {
        val res = _remoteService.userApi.listUserFollowers(username, page, perPage)
        val body = res.body()

        return body ?: emptyList()
    }

    override suspend fun listUserFollowing(username: String, page: Int?, perPage: Int?): List<RemoteUser> {
        val res = _remoteService.userApi.listUserFollowing(username, page, perPage)
        val body = res.body()

        return body ?: emptyList()
    }

    companion object {
        @Volatile
        private var _INSTANCE: RemoteUserDataSource? = null

        fun getInstance(remoteService: RemoteService): RemoteUserDataSource {
            return _INSTANCE ?: synchronized(this) {
                val instance = RemoteUserDataSource(remoteService)
                _INSTANCE = instance

                instance
            }
        }
    }
}
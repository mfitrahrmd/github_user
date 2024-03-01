package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.remote.RemoteService

class UserDetailRepositoryImpl(
    private val _remoteService: RemoteService
    // TODO : db service
) : UserDetailRepository {
    override suspend fun findUserByUsername(username: String): User? {
        val res = _remoteService.userApi.findUserByUsername(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

    override suspend fun listUserFollowers(username: String): List<User>? {
        val res = _remoteService.userApi.listUserFollowers(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

    override suspend fun listUserFollowing(username: String): List<User>? {
        val res = _remoteService.userApi.listUserFollowing(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }
}
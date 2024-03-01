package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.remote.RemoteService
import com.mfitrahrmd.githubuser.utils.ApiExtractor

class UserPopularRepositoryImpl(
    private val _remoteService: RemoteService
    // TODO : db service
) : UserPopularRepository {
    override suspend fun searchPopularUsers(
        location: String,
        page: String?
    ): WithPagination<List<User>> {
        val res = _remoteService.userApi.searchUsers("location:$location")
        if (!res.isSuccessful) throw Exception(res.errorBody().toString())
        val data = res.body()?.items?.toUser()

        return ApiExtractor.getPagination(res.headers(), data)
    }
}
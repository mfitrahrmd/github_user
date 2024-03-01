package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.mapper.toUser
import com.mfitrahrmd.githubuser.repositories.remote.RemoteService
import com.mfitrahrmd.githubuser.utils.ApiExtractor

class UserSearchRepositoryImpl(
    private val _remoteService: RemoteService
    // TODO : db service
) : UserSearchRepository {
    override suspend fun searchUsers(query: String, page: String?): WithPagination<List<User>> {
        val res = _remoteService.userApi.searchUsers(query)
        if (!res.isSuccessful) throw Exception(res.errorBody().toString())
        val data = res.body()?.items?.toUser()

        return ApiExtractor.getPagination(res.headers(), data)
    }
}
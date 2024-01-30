package com.mfitrahrmd.githubuser.repositories.remote

import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.toUser
import com.mfitrahrmd.githubuser.repositories.remote.services.GithubService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRemoteRepository : UserRepository() {
    private val _githubService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubService::class.java)

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    override suspend fun searchUsers(query: String): List<User>? {
        val res = _githubService.searchUsers(query)

        if (!res.isSuccessful) throw Exception(res.message())

        return res.body()?.toUser()
    }

    override suspend fun findUserByUsername(username: String): User? {
        val res = _githubService.findUserByUsername(username)

        if (!res.isSuccessful) throw Exception(res.message())

        return res.body()?.toUser()
    }

    override suspend fun listUserFollowers(username: String): List<User>? {
        TODO("Not yet implemented")
    }

    override suspend fun listUserFollowing(username: String): List<User>? {
        TODO("Not yet implemented")
    }

}
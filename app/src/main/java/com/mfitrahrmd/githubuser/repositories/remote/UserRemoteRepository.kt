package com.mfitrahrmd.githubuser.repositories.remote

import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.toUser
import com.mfitrahrmd.githubuser.repositories.remote.services.GithubService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRemoteRepository : UserRepository() {
    private val  _token = "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"

    private val _githubService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request().newBuilder().addHeader("Authorization", "token $_token").build())
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubService::class.java)

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    override suspend fun searchUsers(query: String): List<User>? {
        val res = _githubService.searchUsers(query)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

    override suspend fun findUserByUsername(username: String): User? {
        val res = _githubService.findUserByUsername(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

    override suspend fun listUserFollowers(username: String): List<User>? {
        val res = _githubService.listUserFollowers(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

    override suspend fun listUserFollowing(username: String): List<User>? {
        val res = _githubService.listUserFollowing(username)

        if (!res.isSuccessful) throw Exception(res.errorBody().toString())

        return res.body()?.toUser()
    }

}
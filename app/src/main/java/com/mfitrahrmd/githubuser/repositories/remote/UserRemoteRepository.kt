package com.mfitrahrmd.githubuser.repositories.remote

import com.mfitrahrmd.githubuser.BuildConfig
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.Pagination
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.toUser
import com.mfitrahrmd.githubuser.repositories.remote.services.GithubService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRemoteRepository : UserRepository() {
    private val _githubService = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor {
            it.proceed(
                it.request().newBuilder().addHeader("Authorization", "token ${BuildConfig.API_KEY}").addHeader("Accept", "application/vnd.github+json").build()
            )
        }.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubService::class.java)

    override suspend fun searchUsers(query: String, page: String?): Pagination<List<User>> {
        val res = _githubService.searchUsers(query, page)
        if (!res.isSuccessful) throw Exception(res.errorBody().toString())
        var prev: Int? = null
        var next: Int? = null
        var first: Int? = null
        var last: Int? = null
        val link = res.headers().get("link")
        if (!link.isNullOrBlank()) {
            link.split(",").forEach {
                if (it.contains("\"prev\"")) {
                    prev = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"next\"")) {
                    next = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"first\"")) {
                    first = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
                if (it.contains("\"last\"")) {
                    last = it.substring(it.indexOf("page=")+5).substringBefore(">").toIntOrNull()
                }
            }
        }

        return Pagination(first, last, prev, next, res.body()?.toUser())
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
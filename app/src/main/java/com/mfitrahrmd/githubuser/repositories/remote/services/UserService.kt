package com.mfitrahrmd.githubuser.repositories.remote.services

import com.mfitrahrmd.githubuser.entities.network.NetworkUser
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.SearchUsers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") q: String,
        @Query("page") page: String? = null,
        @Query("per_page") perPage: String? = null
    ): Response<SearchUsers>

    @GET("users/{username}")
    suspend fun findUserByUsername(@Path("username") username: String): Response<NetworkUser>

    @GET("users/{username}/followers")
    suspend fun listUserFollowers(@Path("username") username: String): Response<List<NetworkUser>>

    @GET("users/{username}/following")
    suspend fun listUserFollowing(@Path("username") username: String): Response<List<NetworkUser>>
}
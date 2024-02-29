package com.mfitrahrmd.githubuser.repositories.remote.services

import com.mfitrahrmd.githubuser.entities.network.NetworkUser
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.GithubSearchUsersResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") q: String, @Query("page") page: String? = null): Response<GithubSearchUsersResponseModel>

    @GET("users/{username}")
    suspend fun findUserByUsername(@Path("username") username: String): Response<NetworkUser>

    @GET("users/{username}/followers")
    suspend fun listUserFollowers(@Path("username") username: String): Response<List<NetworkUser>>

    @GET("users/{username}/following")
    suspend fun listUserFollowing(@Path("username") username: String): Response<List<NetworkUser>>
}
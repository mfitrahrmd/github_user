package com.mfitrahrmd.githubuser.repositories.datasource.remote.services

import com.mfitrahrmd.githubuser.entities.remote.RemoteUser
import com.mfitrahrmd.githubuser.repositories.datasource.remote.responsemodels.SearchUsers
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
    ): Response<SearchUsers?>

    @GET("users/{username}")
    suspend fun findUserByUsername(@Path("username") username: String): Response<RemoteUser?>

    @GET("users/{username}/followers")
    suspend fun listUserFollowers(
        @Path("username") username: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): Response<List<RemoteUser>?>

    @GET("users/{username}/following")
    suspend fun listUserFollowing(
        @Path("username") username: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null
    ): Response<List<RemoteUser>?>
}
package com.mfitrahrmd.githubuser.repositories.remote.services

import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.GithubFindUserByUsernameResponseModel
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.GithubListUserFollowersResponseModel
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.GithubListUserFollowingResponseModel
import com.mfitrahrmd.githubuser.repositories.remote.responsemodels.GithubSearchUsersResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") q: String): Response<GithubSearchUsersResponseModel>

    @GET("users/{username}")
    suspend fun findUserByUsername(@Path("username") username: String): Response<GithubFindUserByUsernameResponseModel>

    @GET("users/{username}/followers")
    suspend fun listUserFollowers(@Path("username") username: String): Response<List<GithubListUserFollowersResponseModel>>

    @GET("users/{username}/following")
    suspend fun listUserFollowing(@Path("username") username: String): Response<List<GithubListUserFollowingResponseModel>>
}
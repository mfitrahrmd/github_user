package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User

interface UserDetailRepository {
    suspend fun findUserByUsername(username: String): User?
    suspend fun listUserFollowers(username: String): List<User>?
    suspend fun listUserFollowing(username: String): List<User>?
}
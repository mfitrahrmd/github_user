package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.models.User

abstract class UserRepository {
    abstract suspend fun searchUsers(query: String): List<User>?

    abstract suspend fun findUserByUsername(username: String): User?

    abstract suspend fun listUserFollowers(username: String): List<User>?

    abstract suspend fun listUserFollowing(username: String): List<User>?
}
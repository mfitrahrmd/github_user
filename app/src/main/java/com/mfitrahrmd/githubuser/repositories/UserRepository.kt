package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User

abstract class UserRepository {
    abstract suspend fun searchUsers(query: String, page: String? = null): Pagination<List<User>>

    abstract suspend fun findUserByUsername(username: String): User?

    abstract suspend fun listUserFollowers(username: String): List<User>?

    abstract suspend fun listUserFollowing(username: String): List<User>?
}
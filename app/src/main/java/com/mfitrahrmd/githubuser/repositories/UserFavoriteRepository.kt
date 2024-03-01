package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User

interface UserFavoriteRepository {
    suspend fun addUserToFavorite(user: User)
    suspend fun removeUserFromFavorite(user: User)
    suspend fun listFavorite()
}
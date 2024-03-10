package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.local.dao.FavoriteUserDao

class UserFavoriteRepositoryImpl(
    private val _userFavoriteDao: FavoriteUserDao
) : UserFavoriteRepository {
    override suspend fun addUserToFavorite(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserFromFavorite(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun listFavorite() {
        TODO("Not yet implemented")
    }
}
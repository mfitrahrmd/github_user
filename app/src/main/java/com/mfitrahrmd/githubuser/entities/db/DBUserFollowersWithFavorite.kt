package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Embedded
import androidx.room.Relation

data class DBUserFollowersWithFavorite(
    @Embedded
    val userFollowers: DBUserFollowers,
    @Relation(parentColumn = "login", entityColumn = "login")
    val favoriteUser: DBFavoriteUser?
)
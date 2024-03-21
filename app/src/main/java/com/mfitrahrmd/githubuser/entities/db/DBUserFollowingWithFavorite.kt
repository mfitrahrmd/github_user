package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Embedded
import androidx.room.Relation

data class DBUserFollowingWithFavorite(
    @Embedded
    val userFollowing: DBUserFollowing,
    @Relation(parentColumn = "login", entityColumn = "login")
    val favoriteUser: DBFavoriteUser?
)
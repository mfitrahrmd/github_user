package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Embedded
import androidx.room.Relation

data class DBPopularUserWithFavorite (
    @Embedded
    val popularUser: DBPopularUser,
    @Relation(parentColumn = "login", entityColumn = "login")
    val favoriteUser: DBFavoriteUser?
)
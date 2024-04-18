package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Embedded
import androidx.room.Relation

data class DBDetailUserWithFavorite(
    @Embedded
    val detailUser: DBDetailUser?,
    @Relation(parentColumn = "login", entityColumn = "login")
    val favoriteUser: DBFavoriteUser?
)
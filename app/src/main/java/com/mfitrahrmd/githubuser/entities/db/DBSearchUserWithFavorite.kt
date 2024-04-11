package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Embedded
import androidx.room.Relation

data class DBSearchUserWithFavorite(
    @Embedded
    val searchUser: DBSearchUser,
    @Relation(parentColumn = "login", entityColumn = "login")
    val favoriteUser: DBFavoriteUser?
)
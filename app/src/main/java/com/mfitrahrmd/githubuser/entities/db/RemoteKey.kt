package com.mfitrahrmd.githubuser.entities.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKey(
    @PrimaryKey
    val userId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
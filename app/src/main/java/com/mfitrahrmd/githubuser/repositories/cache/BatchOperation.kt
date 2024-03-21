package com.mfitrahrmd.githubuser.repositories.cache

import androidx.room.Insert

interface BatchOperation<LocalEntity> {
    @Insert
    suspend fun insertMany(items: List<LocalEntity>)
}
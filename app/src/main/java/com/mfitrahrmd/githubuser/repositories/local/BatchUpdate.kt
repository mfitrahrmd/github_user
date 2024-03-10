package com.mfitrahrmd.githubuser.repositories.local

import androidx.room.Ignore
import androidx.room.Insert

interface BatchUpdate<LocalEntity> {
    @Insert
    fun insertMany(items: List<LocalEntity>)
    @Ignore
    fun deleteAll() // must be override by the inheritor
}
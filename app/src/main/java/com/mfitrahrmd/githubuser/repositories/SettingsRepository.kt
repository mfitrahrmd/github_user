package com.mfitrahrmd.githubuser.repositories

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setIsDarkTheme(setter: (Boolean) -> Boolean)
}
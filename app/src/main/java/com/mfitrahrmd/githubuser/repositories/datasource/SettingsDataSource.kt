package com.mfitrahrmd.githubuser.repositories.datasource

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setIsDarkTheme(setter: (Boolean) -> Boolean)
}
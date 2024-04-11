package com.mfitrahrmd.githubuser.repositories

import com.mfitrahrmd.githubuser.repositories.datasource.SettingsDataSource
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val _dataSource: SettingsDataSource) : SettingsRepository {
    override fun getIsDarkTheme(): Flow<Boolean> = _dataSource.getIsDarkTheme()

    override suspend fun setIsDarkTheme(setter: (Boolean) -> Boolean) {
        _dataSource.setIsDarkTheme(setter)
    }
}
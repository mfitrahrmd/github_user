package com.mfitrahrmd.githubuser.ui.main.fragments.settings

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.repositories.SettingsRepository

class SettingsViewModel(
    private val _settingsRepository: SettingsRepository
) : ViewModel() {
    suspend fun setIsDarkTheme(isDarkTheme: Boolean) {
        _settingsRepository.setIsDarkTheme {
            isDarkTheme
        }
    }

    fun getIsDarkTheme() = _settingsRepository.getIsDarkTheme()
}
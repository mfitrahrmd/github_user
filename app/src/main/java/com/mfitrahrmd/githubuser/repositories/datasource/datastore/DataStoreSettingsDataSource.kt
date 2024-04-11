package com.mfitrahrmd.githubuser.repositories.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mfitrahrmd.githubuser.repositories.datasource.SettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsDataSource private constructor(private val _dataStore: DataStore<Preferences>) :
    SettingsDataSource {
    private val _themeKey = booleanPreferencesKey("is_dark_theme")

    override fun getIsDarkTheme(): Flow<Boolean> {
        return _dataStore.data.map { preference ->
            preference[_themeKey] ?: false
        }
    }

    override suspend fun setIsDarkTheme(setter: (Boolean) -> Boolean) {
        _dataStore.edit { preference ->
            preference[_themeKey] = setter(preference[_themeKey] ?: false)
        }
    }

    companion object {
        @Volatile
        private var _INSTANCE: DataStoreSettingsDataSource? = null

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreSettingsDataSource {
            return _INSTANCE ?: synchronized(this) {
                val instance = DataStoreSettingsDataSource(dataStore)
                _INSTANCE = instance

                instance
            }
        }
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

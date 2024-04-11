package com.mfitrahrmd.githubuser

import android.content.Context
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.DetailUserRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.PopularUsersRepository
import com.mfitrahrmd.githubuser.repositories.PopularUsersRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.SettingsRepository
import com.mfitrahrmd.githubuser.repositories.SettingsRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.cache.database.UserDatabase
import com.mfitrahrmd.githubuser.repositories.datasource.SettingsDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.UserDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.datastore.DataStoreSettingsDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.datastore.dataStore
import com.mfitrahrmd.githubuser.repositories.datasource.inmemory.InMemoryUserDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.RemoteUserDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.RemoteService

interface AppContainer {
    val searchUsersRepository: SearchUsersRepository
    val detailUserRepository: DetailUserRepository
    val userFavoriteRepository: UserFavoriteRepository
    val popularUsersRepository: PopularUsersRepository
    val settingsRepository: SettingsRepository
}

class AppDataContainer(private val _context: Context) : AppContainer {
    private val _remoteService: RemoteService by lazy {
        RemoteService.getInstance()
    }
    private val _userDatabase: UserDatabase by lazy {
        UserDatabase.getInstance(_context)
    }
    private val _remoteUserDataSource: UserDataSource by lazy {
        RemoteUserDataSource.getInstance(_remoteService)
    }
    /*
    * InMemory UserDataSource implementation for testing
    * */
    private val _inMemoryUserDataSource: UserDataSource by lazy {
        InMemoryUserDataSource.getInstance()
    }
    private val _dataStoreSettingsDataSource: SettingsDataSource by lazy {
        DataStoreSettingsDataSource.getInstance(_context.dataStore)
    }
    override val searchUsersRepository: SearchUsersRepository by lazy {
        SearchUsersRepositoryImpl(
            _remoteUserDataSource,
            _userDatabase.searchUserDao()
        )
    }
    override val detailUserRepository: DetailUserRepository by lazy {
        DetailUserRepositoryImpl(_remoteUserDataSource, _userDatabase.detailUserDao(), _userDatabase.userFollowingDao(), _userDatabase.userFollowersDao(), _userDatabase.favoriteUserDao())
    }
    override val userFavoriteRepository: UserFavoriteRepository by lazy {
        UserFavoriteRepositoryImpl(_userDatabase.favoriteUserDao())
    }
    override val popularUsersRepository: PopularUsersRepository by lazy {
        PopularUsersRepositoryImpl(
            _remoteUserDataSource,
            _userDatabase.popularUserDao()
        )
    }
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(_dataStoreSettingsDataSource)
    }
}
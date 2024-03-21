package com.mfitrahrmd.githubuser

import android.content.Context
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.DetailUserRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserPopularRepository
import com.mfitrahrmd.githubuser.repositories.UserPopularRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.cache.database.UserDatabase
import com.mfitrahrmd.githubuser.repositories.datasource.DataSource
import com.mfitrahrmd.githubuser.repositories.datasource.inmemory.InMemoryDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.RemoteDataSource
import com.mfitrahrmd.githubuser.repositories.datasource.remote.RemoteService

interface AppContainer {
    val searchUsersRepository: SearchUsersRepository
    val detailUserRepository: DetailUserRepository
    val userFavoriteRepository: UserFavoriteRepository
    val userPopularRepository: UserPopularRepository
}

class AppDataContainer(private val _context: Context) : AppContainer {
    private val _remoteService: RemoteService by lazy {
        RemoteService.getInstance()
    }
    private val _userDatabase: UserDatabase by lazy {
        UserDatabase.getInstance(_context)
    }
    private val _remoteDataSource: DataSource by lazy {
        RemoteDataSource.getInstance(_remoteService)
    }
    /*
    * InMemory DataSource implementation for testing
    * */
    private val _inMemoryDataSource: DataSource by lazy {
        InMemoryDataSource.getInstance()
    }
    override val searchUsersRepository: SearchUsersRepository by lazy {
        SearchUsersRepositoryImpl(
            _inMemoryDataSource,
            _userDatabase.searchUserDao(),
            _userDatabase.favoriteUserDao(),
            _userDatabase.remoteKeyDao()
        )
    }
    override val detailUserRepository: DetailUserRepository by lazy {
        DetailUserRepositoryImpl(_inMemoryDataSource, _userDatabase.detailUserDao(), _userDatabase.userFollowingDao(), _userDatabase.userFollowersDao(), _userDatabase.favoriteUserDao())
    }
    override val userFavoriteRepository: UserFavoriteRepository by lazy {
        UserFavoriteRepositoryImpl(_userDatabase.favoriteUserDao())
    }
    override val userPopularRepository: UserPopularRepository by lazy {
        UserPopularRepositoryImpl(
            _inMemoryDataSource,
            _userDatabase.popularUserDao(),
            _userDatabase.favoriteUserDao()
        )
    }
}
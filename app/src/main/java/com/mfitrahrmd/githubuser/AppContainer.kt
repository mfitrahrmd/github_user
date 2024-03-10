package com.mfitrahrmd.githubuser

import android.content.Context
import com.mfitrahrmd.githubuser.repositories.UserDetailRepository
import com.mfitrahrmd.githubuser.repositories.UserDetailRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserPopularRepository
import com.mfitrahrmd.githubuser.repositories.UserPopularRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.UserSearchRepository
import com.mfitrahrmd.githubuser.repositories.UserSearchRepositoryImpl
import com.mfitrahrmd.githubuser.repositories.local.database.UserDatabase
import com.mfitrahrmd.githubuser.repositories.remote.RemoteService

interface AppContainer {
    val userSearchRepository: UserSearchRepository
    val userDetailRepository: UserDetailRepository
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
    override val userSearchRepository: UserSearchRepository by lazy {
        UserSearchRepositoryImpl(
            _remoteService,
            _userDatabase.searchUserDao(),
            _userDatabase.favoriteUserDao(),
            _userDatabase.remoteKeyDao()
        )
    }
    override val userDetailRepository: UserDetailRepository by lazy {
        UserDetailRepositoryImpl(_remoteService)
    }
    override val userFavoriteRepository: UserFavoriteRepository by lazy {
        UserFavoriteRepositoryImpl(_userDatabase.favoriteUserDao())
    }
    override val userPopularRepository: UserPopularRepository by lazy {
        UserPopularRepositoryImpl(_remoteService.userApi, _userDatabase.popularUserDao(), _userDatabase.favoriteUserDao())
    }
}
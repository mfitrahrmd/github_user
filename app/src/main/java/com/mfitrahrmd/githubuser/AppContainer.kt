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
    override val userSearchRepository: UserSearchRepository by lazy {
        UserSearchRepositoryImpl(_remoteService)
    }
    override val userDetailRepository: UserDetailRepository by lazy {
        UserDetailRepositoryImpl(_remoteService)
    }
    override val userFavoriteRepository: UserFavoriteRepository by lazy {
        UserFavoriteRepositoryImpl()
    }
    override val userPopularRepository: UserPopularRepository by lazy {
        UserPopularRepositoryImpl(_remoteService)
    }
}
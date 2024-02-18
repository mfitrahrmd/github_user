package com.mfitrahrmd.githubuser

import android.content.Context
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.repositories.remote.UserRemoteRepository

interface AppContainer {
    val userRepository: UserRepository
}

class AppDataContainer(private val _context: Context) : AppContainer {
    override val userRepository: UserRepository by lazy {
        UserRemoteRepository()
    }
}
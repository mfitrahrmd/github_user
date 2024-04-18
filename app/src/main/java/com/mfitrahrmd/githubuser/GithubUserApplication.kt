package com.mfitrahrmd.githubuser

import android.app.Application

class GithubUserApplication : Application() {
    val appContainer: AppContainer  = AppDataContainer(this)
}
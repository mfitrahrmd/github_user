package com.mfitrahrmd.githubuser

import android.app.Application

class GithubUserApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppDataContainer(this)
    }
}
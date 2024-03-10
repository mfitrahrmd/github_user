package com.mfitrahrmd.githubuser.repositories.remote

import com.mfitrahrmd.githubuser.BuildConfig
import com.mfitrahrmd.githubuser.repositories.remote.services.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteService private constructor() {
    private val _retrofit: Retrofit
    private var _userApi: UserService? = null

    val userApi: UserService
        get() {
            if (_userApi == null) {
                _userApi = _retrofit.create(UserService::class.java)
            }

            return _userApi!!
        }

    init {
        val client = OkHttpClient.Builder().addInterceptor {
            it.proceed(
                it.request().newBuilder().addHeader("Authorization", "token ${BuildConfig.API_KEY}")
                    .addHeader("Accept", "application/vnd.github+json").build()
            )
        }
        _retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        @Volatile
        private var _INSTANCE: RemoteService? = null

        fun getInstance(): RemoteService {
            return _INSTANCE ?: synchronized(this) {
                val instance = RemoteService()
                _INSTANCE = instance

                instance
            }
        }
    }
}
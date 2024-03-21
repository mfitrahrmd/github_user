package com.mfitrahrmd.githubuser.repositories

sealed class Result<out R> private constructor(val message: String?) {
    class Success<out T>(val data: T, message: String? = null) : Result<T>(message)
    class Error(message: String? = null) : Result<Nothing>(message)
    class Loading(message: String? = null) : Result<Nothing>(message)
}
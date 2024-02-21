package com.mfitrahrmd.githubuser.base

sealed class BaseState<T>(val message: String?, val data: T?) {
    class Idle<T>(message: String? = null, data: T? = null) : BaseState<T>(message, data)
    class Loading<T>(message: String?, data: T?) : BaseState<T>(message, data)
    class Error<T>(message: String?, data: T?) : BaseState<T>(message, data)
    class Success<T>(message: String?, data: T?) : BaseState<T>(message, data)
}

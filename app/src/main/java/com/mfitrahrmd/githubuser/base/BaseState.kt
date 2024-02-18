package com.mfitrahrmd.githubuser.base

sealed class BaseState<T>(val message: String?) {
    class Loading<T>(message: String?) : BaseState<T>(message) {
        constructor() : this(null)
    }

    class Error<T>(message: String?) : BaseState<T>(message) {
        constructor() : this(null)
    }

    class Success<T>(message: String?, val data: T?) : BaseState<T>(message) {
        constructor(data: T?) : this(null, data)
        constructor() : this(null, null)
        constructor(message: String) : this(message, null)
    }
}

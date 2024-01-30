package com.mfitrahrmd.githubuser.ui.main.fragments.home

sealed class UiState<T>(val message: String?) {
    class Loading<T>(message: String?) : UiState<T>(message) {
        constructor() : this(null)
    }

    class Error<T>(message: String?) : UiState<T>(message) {
        constructor() : this(null)
    }

    class Success<T>(message: String?, val data: T?) : UiState<T>(message) {
        constructor(data: T?) : this(null, data)
        constructor() : this(null, null)
        constructor(message: String) : this(message, null)
    }
}

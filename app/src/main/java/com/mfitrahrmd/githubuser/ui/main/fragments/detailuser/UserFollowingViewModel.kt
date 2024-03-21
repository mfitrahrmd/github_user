package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch

class UserFollowingViewModel(private val _detailUserRepository: DetailUserRepository) :
    ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userFollowing = _username.flatMapConcat {
        _detailUserRepository.getFollowing(it).cachedIn(viewModelScope)
    }
}
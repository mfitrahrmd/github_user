package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserFollowersViewModel(private val _detailUserRepository: DetailUserRepository) :
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
    val userFollowers = _username.flatMapConcat {
        _detailUserRepository.getFollowers(it).cachedIn(viewModelScope)
    }
}
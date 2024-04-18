package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class UserFollowingViewModel(
    private val _detailUserRepository: DetailUserRepository,
) :
    ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String>
        get() = _username
    private val _userFollowing: MutableStateFlow<PagingData<User>> = MutableStateFlow(PagingData.empty())
    val userFollowing: StateFlow<PagingData<User>>
        get() = _userFollowing

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    init {
        viewModelScope.launch {
            _username.collectLatest {
                try {
                    val followingPage =
                        _detailUserRepository.getFollowing(it).cachedIn(viewModelScope)
                    _userFollowing.emitAll(followingPage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
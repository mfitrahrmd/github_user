package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFollowingViewModel(private val _detailUserRepository: DetailUserRepository) :
    ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value
    private val _userFollowing: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val userFollowing: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _userFollowing

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    init {
        viewModelScope.launch {
            _username.collectLatest {
                _userFollowing.value = BaseState.Loading(null, null)
                try {
                    val followingPage = _detailUserRepository.getFollowing(it).cachedIn(viewModelScope)
                    _userFollowing.value = BaseState.Success(null, followingPage)
                } catch (e: Exception) {
                    BaseState.Error(e.message, _userFollowing.value.data)
                }
            }
        }
    }
}
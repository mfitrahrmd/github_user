package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    suspend fun getUserFollowing() {
        _detailUserRepository.getFollowing(username).cachedIn(viewModelScope).collect {
            _userFollowing.value = BaseState.Loading(null, null)
            delay(5_000L)
            _userFollowing.value = try {
                BaseState.Success(null, _detailUserRepository.getFollowing(username))
            } catch (e: Exception) {
                BaseState.Error(e.message, null)
            }
        }
    }
}
package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UserFollowingViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private val _userFollowingState: MutableStateFlow<BaseState<List<User>>> = MutableStateFlow(
        BaseState.Success()
    )
    val userFollowingState: StateFlow<BaseState<List<User>>> = _userFollowingState

    suspend fun initData(username: String) {
        when (_userFollowingState.value) {
            is BaseState.Success -> {
                val state = (_userFollowingState.value as BaseState.Success<List<User>>)
                if (state.data.isNullOrEmpty()) {
                    getListFollowing(username)
                }
            }

            else -> {
                getListFollowing(username)
            }
        }
    }

    suspend fun getListFollowing(username: String) {
        try {
            _userFollowingState.update {
                BaseState.Loading()
            }
            val following = _userRepository.listUserFollowing(username)
            _userFollowingState.update {
                BaseState.Success(following)
            }
        } catch (e: Exception) {
            _userFollowingState.update {
                BaseState.Error(e.message)
            }
        }
    }
}
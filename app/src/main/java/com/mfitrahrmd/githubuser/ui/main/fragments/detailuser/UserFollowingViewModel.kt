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
        BaseState.Idle()
    )
    val userFollowingState: StateFlow<BaseState<List<User>>>
        get() = _userFollowingState

    var username: String = ""

    suspend fun initData() {
        if (_userFollowingState.value.data == null) {
            getListFollowing()
        }
    }

    suspend fun getListFollowing() {
        try {
            _userFollowingState.update {
                BaseState.Loading(null, null)
            }
            val following = _userRepository.listUserFollowing(username)
            _userFollowingState.update {
                BaseState.Success(null, following)
            }
        } catch (e: Exception) {
            _userFollowingState.update {
                BaseState.Error(e.message, null)
            }
        }
    }
}
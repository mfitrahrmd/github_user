package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UserFollowersViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private val _userFollowersState: MutableStateFlow<BaseState<List<User>>> = MutableStateFlow(
        BaseState.Idle()
    )
    val userFollowersState: StateFlow<BaseState<List<User>>>
        get() = _userFollowersState

    var username: String = ""

    suspend fun initData() {
        if (_userFollowersState.value.data == null) {
            getListFollowers()
        }
    }


    suspend fun getListFollowers() {
        try {
            _userFollowersState.update {
                BaseState.Loading(null, null)
            }
            val followers = _userRepository.listUserFollowers(username)
            _userFollowersState.update {
                BaseState.Success(null, followers)
            }
        } catch (e: Exception) {
            _userFollowersState.update {
                BaseState.Error(e.message, null)
            }
        }
    }
}
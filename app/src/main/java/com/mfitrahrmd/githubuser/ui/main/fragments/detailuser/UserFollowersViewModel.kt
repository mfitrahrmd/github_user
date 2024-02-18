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
        BaseState.Success()
    )
    val userFollowersState: StateFlow<BaseState<List<User>>> = _userFollowersState

    suspend fun getListFollowers(username: String) {
        try {
            _userFollowersState.update {
                BaseState.Loading()
            }
            val followers = _userRepository.listUserFollowers(username)
            _userFollowersState.update {
                BaseState.Success(followers)
            }
        } catch (e: Exception) {
            _userFollowersState.update {
                BaseState.Error(e.message)
            }
        }
    }
}
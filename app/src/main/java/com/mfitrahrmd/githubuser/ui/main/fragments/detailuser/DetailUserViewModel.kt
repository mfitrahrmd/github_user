package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.adapters.UserFollowingFollowersAdapter
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.base.BaseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailUserViewModel(private val _userRepository: UserRepository) : ViewModel(), UserFollowingFollowersAdapter.DataFlow {
    private val _userState: MutableStateFlow<BaseState<User>> = MutableStateFlow(BaseState.Success())
    val userState: StateFlow<BaseState<User>> = _userState

    private val _userFollowingState: MutableStateFlow<BaseState<List<User>>> = MutableStateFlow(
        BaseState.Success())
    override val userFollowingState: StateFlow<BaseState<List<User>>> = _userFollowingState

    private val _userFollowersState: MutableStateFlow<BaseState<List<User>>> = MutableStateFlow(
        BaseState.Success())
    override val userFollowersState: StateFlow<BaseState<List<User>>> = _userFollowersState

    suspend fun getUser(username: String) {
        try {
            _userState.update {
                BaseState.Loading()
            }

            delay(3_000L)
            val user = _userRepository.findUserByUsername(username)

            _userState.update {
                BaseState.Success(user)
            }
        } catch (e: Exception) {
            _userState.update {
                BaseState.Error(e.message)
            }
        }
    }

    suspend fun getListFollowing(username: String) {
        try {
            _userFollowingState.update {
                BaseState.Loading()
            }

            delay(3_000L)
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

    suspend fun getListFollowers(username: String) {
        try {
            _userFollowersState.update {
                BaseState.Loading()
            }

            delay(3_000L)
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
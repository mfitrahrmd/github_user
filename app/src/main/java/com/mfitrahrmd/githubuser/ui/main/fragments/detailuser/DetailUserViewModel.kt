package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.adapters.UserFollowingFollowersAdapter
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailUserViewModel(private val _userRepository: UserRepository) : ViewModel(), UserFollowingFollowersAdapter.DataFlow {
    private val _userState: MutableStateFlow<UiState<User>> = MutableStateFlow(UiState.Success())
    val userState: StateFlow<UiState<User>> = _userState

    private val _userFollowingState: MutableStateFlow<UiState<List<User>>> = MutableStateFlow(UiState.Success())
    override val userFollowingState: StateFlow<UiState<List<User>>> = _userFollowingState

    private val _userFollowersState: MutableStateFlow<UiState<List<User>>> = MutableStateFlow(UiState.Success())
    override val userFollowersState: StateFlow<UiState<List<User>>> = _userFollowersState

    suspend fun getUser(username: String) {
        try {
            _userState.update {
                UiState.Loading()
            }

            delay(3_000L)
            val user = _userRepository.findUserByUsername(username)

            _userState.update {
                UiState.Success(user)
            }
        } catch (e: Exception) {
            _userState.update {
                UiState.Error(e.message)
            }
        }
    }

    suspend fun getListFollowing(username: String) {
        try {
            _userFollowingState.update {
                UiState.Loading()
            }

            delay(3_000L)
            val following = _userRepository.listUserFollowing(username)

            _userFollowingState.update {
                UiState.Success(following)
            }
        } catch (e: Exception) {
            _userFollowingState.update {
                UiState.Error(e.message)
            }
        }
    }

    suspend fun getListFollowers(username: String) {
        try {
            _userFollowersState.update {
                UiState.Loading()
            }

            delay(3_000L)
            val followers = _userRepository.listUserFollowers(username)

            _userFollowersState.update {
                UiState.Success(followers)
            }
        } catch (e: Exception) {
            _userFollowersState.update {
                UiState.Error(e.message)
            }
        }
    }
}
package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DetailUserViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private val _userState: MutableStateFlow<BaseState<User>> =
        MutableStateFlow(BaseState.Success())
    val userState: StateFlow<BaseState<User>> = _userState

    suspend fun getUser(username: String) {
        try {
            _userState.update {
                BaseState.Loading()
            }
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
}
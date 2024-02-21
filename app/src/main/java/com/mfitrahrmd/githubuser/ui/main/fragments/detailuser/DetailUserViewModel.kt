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
        MutableStateFlow(BaseState.Idle())
    val userState: StateFlow<BaseState<User>>
        get() = _userState

    var username: String = ""

    suspend fun initData() {
        if (_userState.value.data == null) {
            getUser()
        }
    }

    suspend fun getUser() {
        try {
            _userState.update {
                BaseState.Loading(null, null)
            }
            val user = _userRepository.findUserByUsername(username)
            _userState.update {
                BaseState.Success(null, user)
            }
        } catch (e: Exception) {
            _userState.update {
                BaseState.Error(e.message, null)
            }
        }
    }
}
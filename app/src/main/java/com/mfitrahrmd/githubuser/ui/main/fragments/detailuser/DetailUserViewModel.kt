package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailUserViewModel(private val _detailUserRepository: DetailUserRepository) : ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String>
        get() = _username
    private val _detailUser: MutableStateFlow<BaseState<User>> =
        MutableStateFlow(BaseState.Idle())
    val detailUser: StateFlow<BaseState<User>>
        get() = _detailUser

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    init {
        viewModelScope.launch {
            _username.collectLatest {
                _detailUserRepository.get(it).collect { result ->
                    _detailUser.value = when (result) {
                        is Result.Error -> BaseState.Error(result.message, null)
                        is Result.Loading -> BaseState.Loading(null, null)
                        is Result.Success -> BaseState.Success(null, result.data)
                    }
                }
            }
        }
    }
}
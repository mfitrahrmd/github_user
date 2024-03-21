package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailUserViewModel(private val _detailUserRepository: DetailUserRepository) : ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value
    private val _detailUser: MutableStateFlow<BaseState<User>> =
        MutableStateFlow(BaseState.Idle())
    val detailUser: StateFlow<BaseState<User>>
        get() = _detailUser

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    suspend fun getDetailUser() {
        _detailUserRepository.get(username).collect {
            _detailUser.value = when(it) {
                is Result.Error -> BaseState.Error(it.message, null)
                is Result.Loading -> BaseState.Loading(null, null)
                is Result.Success -> BaseState.Success(null, it.data)
            }
        }
    }
}
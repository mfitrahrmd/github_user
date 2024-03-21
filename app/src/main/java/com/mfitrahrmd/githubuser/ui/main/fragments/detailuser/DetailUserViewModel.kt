package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailUserViewModel(private val _detailUserRepository: DetailUserRepository) : ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val detailUser = _username.flatMapConcat {
        _detailUserRepository.get(it)
            .map { result ->
                when (result) {
                    is Result.Error -> BaseState.Error(result.message, null)
                    is Result.Loading -> BaseState.Loading(null, null)
                    is Result.Success -> BaseState.Success(null, result.data)
                }
            }
            .catch {  e ->
                Log.d("USER NOT FOUND", e.message ?: "")
            }
    }.onStart {
        emit(BaseState.Idle())
    }
}
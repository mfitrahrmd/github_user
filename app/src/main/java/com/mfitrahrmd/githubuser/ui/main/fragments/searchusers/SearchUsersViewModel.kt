package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.PopularUsersRepository
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class SearchUsersViewModel(
    private val _searchUsersRepository: SearchUsersRepository,
    private val _popularUsersRepository: PopularUsersRepository,
) : ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value
    private val _searchUsersState: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val searchUsersState: StateFlow<PagingData<User>>
        get() = _searchUsersState
    private val _popularUsersState: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val popularUsersState: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _popularUsersState

    init {
        viewModelScope.launch {
            _username.collectLatest {
                try {
                    val followingPage =
                        _searchUsersRepository.get(it).cachedIn(viewModelScope)
                    _searchUsersState.emitAll(followingPage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    fun getPopularUsers() {
        viewModelScope.launch {
            _popularUsersState.value = BaseState.Loading(null, null)
            try {
                val popularUsersPage =
                    _popularUsersRepository.get(POPULAR_USERS_LOCATION).cachedIn(viewModelScope)
                _popularUsersState.value = BaseState.Success(null, popularUsersPage)
            } catch (e: Exception) {
                BaseState.Error(e.message, _popularUsersState.value.data)
            }
        }
    }

    companion object {
        const val POPULAR_USERS_LOCATION = "indonesia"
    }
}
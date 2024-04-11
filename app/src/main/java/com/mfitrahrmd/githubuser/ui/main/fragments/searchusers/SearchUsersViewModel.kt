package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import com.mfitrahrmd.githubuser.repositories.PopularUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchUsersViewModel(
    private val _searchUsersRepository: SearchUsersRepository,
    private val _popularUsersRepository: PopularUsersRepository,
    private val _userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {
    private val _searchUsersState: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val searchUsersState: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _searchUsersState
    private val _popularUsersState: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val popularUsersState: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _popularUsersState


    fun searchUsers(username: String) {
        viewModelScope.launch {
            _searchUsersState.value = BaseState.Loading(null, null)
            try {
                val searchUsersPage = _searchUsersRepository.get(username).cachedIn(viewModelScope)
                _searchUsersState.value = BaseState.Success(null, searchUsersPage)
            } catch (e: Exception) {
                BaseState.Error(e.message, _searchUsersState.value.data)
            }
        }
    }

    fun getPopularUsers() {
        viewModelScope.launch {
            _popularUsersState.value = BaseState.Loading(null, null)
            try {
                val popularUsersPage = _popularUsersRepository.get(POPULAR_USERS_LOCATION).cachedIn(viewModelScope)
                _popularUsersState.value = BaseState.Success(null, popularUsersPage)
            } catch (e: Exception) {
                BaseState.Error(e.message, _searchUsersState.value.data)
            }
        }
    }

    fun addToFavorite(user: User) {
        viewModelScope.launch {
            _userFavoriteRepository.add(user)
        }
    }

    fun removeFromFavorite(user: User) {
        viewModelScope.launch {
            _userFavoriteRepository.remove(user)
        }
    }

    companion object {
        const val POPULAR_USERS_LOCATION = "indonesia"
    }
}
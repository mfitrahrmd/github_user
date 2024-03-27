package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import com.mfitrahrmd.githubuser.repositories.UserPopularRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchUsersViewModel(
    private val _searchUsersRepository: SearchUsersRepository,
    private val _userPopularRepository: UserPopularRepository,
    private val _userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {
    private val _searchUsersState: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val searchUsersState: StateFlow<PagingData<User>>
        get() = _searchUsersState
    private val _popularUsersState: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val popularUsersState: StateFlow<PagingData<User>>
        get() = _popularUsersState


    fun searchUsers(username: String) {
        viewModelScope.launch {
            try {
                _searchUsersRepository.get(username).cachedIn(viewModelScope).collect {
                    _searchUsersState.value = it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPopularUsers() {
        viewModelScope.launch {
            try {
                _userPopularRepository.get(POPULAR_USERS_LOCATION).cachedIn(viewModelScope)
                    .collect {
                        _popularUsersState.value = it
                    }
            } catch (e: Exception) {
                e.printStackTrace()
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
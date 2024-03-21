package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.util.Log
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
            _searchUsersRepository.get(username).cachedIn(viewModelScope).collect {
                _searchUsersState.value = it
            }
        }
    }

    fun getPopularUsers() {
        viewModelScope.launch {
            _userPopularRepository.get(POPULAR_USERS_LOCATION).cachedIn(viewModelScope)
                .collect {
                    _popularUsersState.value = it
                }
        }
    }

    fun addToFavorite(user: User) {
        Log.d("ADD", user.toString())
        viewModelScope.launch {
            _userFavoriteRepository.add(user)
        }
    }

    fun removeFromFavorite(user: User) {
        Log.d("REMOVE", user.toString())
        viewModelScope.launch {
            _userFavoriteRepository.remove(user)
        }
    }

    companion object {
        const val POPULAR_USERS_LOCATION = "indonesia"
    }
}
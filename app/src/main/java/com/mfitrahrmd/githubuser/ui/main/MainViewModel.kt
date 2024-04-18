package com.mfitrahrmd.githubuser.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.PopularUsersRepository
import com.mfitrahrmd.githubuser.repositories.SearchUsersRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class MainViewModel(
    private val _userFavoriteRepository: UserFavoriteRepository,
    private val _searchUsersRepository: SearchUsersRepository,
    private val _popularUsersRepository: PopularUsersRepository
) : ViewModel() {
    private val _searchUsername: MutableStateFlow<String> = MutableStateFlow("")
    val searchUsername: StateFlow<String>
        get() = _searchUsername
    private val _searchUsers: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val searchUsers: StateFlow<PagingData<User>>
        get() = _searchUsers
    private val _popularUsers: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val popularUsers: StateFlow<PagingData<User>>
        get() = _popularUsers

    init {
        viewModelScope.launch {
            try {
                val popularUsersPage =
                    _popularUsersRepository.get(POPULAR_USERS_LOCATION).cachedIn(viewModelScope)
                _popularUsers.emitAll(popularUsersPage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewModelScope.launch {
            _searchUsername.collectLatest {
                if (it.isEmpty()) return@collectLatest
                try {
                    val searchUsersPage =
                        _searchUsersRepository.get(it).cachedIn(viewModelScope)
                    _searchUsers.emitAll(searchUsersPage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setSearchUsername(username: String) {
        viewModelScope.launch {
            _searchUsername.emit(username)
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
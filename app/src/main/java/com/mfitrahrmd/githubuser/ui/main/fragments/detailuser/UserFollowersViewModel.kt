package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFollowersViewModel(
    private val _detailUserRepository: DetailUserRepository,
    private val _userFavoriteRepository: UserFavoriteRepository
) :
    ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: String
        get() = _username.value
    private val _userFollowers: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val userFollowers: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _userFollowers

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
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

    init {
        viewModelScope.launch {
            _username.collectLatest {
                _userFollowers.value = BaseState.Loading(null, null)
                try {
                    val followersPage =
                        _detailUserRepository.getFollowers(it).cachedIn(viewModelScope)
                    _userFollowers.value = BaseState.Success(null, followersPage)
                } catch (e: Exception) {
                    BaseState.Error(e.message, _userFollowers.value.data)
                }
            }
        }
    }
}
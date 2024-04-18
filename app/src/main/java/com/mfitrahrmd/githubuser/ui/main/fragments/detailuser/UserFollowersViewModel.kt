package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class UserFollowersViewModel(
    private val _detailUserRepository: DetailUserRepository,
) :
    ViewModel() {
    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String>
        get() = _username
    private var _userFollowers: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val userFollowers: StateFlow<PagingData<User>>
        get() = _userFollowers

    fun setUsername(username: String) {
        viewModelScope.launch {
            _username.emit(username)
        }
    }

    init {
        viewModelScope.launch {
            _username.collectLatest {
                try {
                    val followersPage =
                        _detailUserRepository.getFollowers(it).cachedIn(viewModelScope)
                    _userFollowers.emitAll(followersPage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
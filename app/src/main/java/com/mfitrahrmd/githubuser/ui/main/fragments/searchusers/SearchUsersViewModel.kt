package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.base.BaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private var _searchUsersState: MutableStateFlow<BaseState<List<User>?>> =
        MutableStateFlow(BaseState.Success())
    val searchUsersState: StateFlow<BaseState<List<User>?>> = _searchUsersState

    private var _popularIndoUsersState: MutableStateFlow<BaseState<List<User>?>> = MutableStateFlow(
        BaseState.Success()
    )
    val popularIndoUsersState: StateFlow<BaseState<List<User>?>> = _popularIndoUsersState

    init {
        viewModelScope.launch {
            getPopularIndoUsers()
        }
    }

    suspend fun searchUsers(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchUsersState.update {
                    BaseState.Loading()
                }
                val foundUsers = _userRepository.searchUsers(query)
                _searchUsersState.update {
                    BaseState.Success(foundUsers)
                }
            } catch (e: Exception) {
                _searchUsersState.update {
                    BaseState.Error(e.message)
                }
            }
        }
    }

    suspend fun getPopularIndoUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _popularIndoUsersState.update {
                    BaseState.Loading()
                }
                val foundUsers = _userRepository.searchUsers(POPULAR_INDO_USERS_QUERY)
                val detailUsers: MutableList<User> = mutableListOf()
                foundUsers?.forEach {
                    // TODO : fix rate limit by authenticate into Github
                    val foundUser = _userRepository.findUserByUsername(it.login)
                    if (foundUser != null) {
                        detailUsers.add(foundUser)
                    }
                }
                _popularIndoUsersState.update {
                    BaseState.Success(detailUsers)
                }
            } catch (e: Exception) {
                _popularIndoUsersState.update {
                    BaseState.Error(e.message)
                }
            }
        }
    }

    companion object {
        const val POPULAR_INDO_USERS_QUERY = "location:indonesia"
    }
}
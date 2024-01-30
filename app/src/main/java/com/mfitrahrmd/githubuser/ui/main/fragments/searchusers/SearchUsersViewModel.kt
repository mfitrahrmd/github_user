package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import com.mfitrahrmd.githubuser.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private var _searchUsersState: MutableStateFlow<UiState<List<User>?>> =
        MutableStateFlow(UiState.Success())
    val searchUsersState: StateFlow<UiState<List<User>?>> = _searchUsersState

    private var _popularIndoUsersState: MutableStateFlow<UiState<List<User>?>> = MutableStateFlow(
        UiState.Success()
    )
    val popularIndoUsersState: StateFlow<UiState<List<User>?>> = _popularIndoUsersState

    init {
        viewModelScope.launch {
            getPopularIndoUsers()
        }
    }

    suspend fun searchUsers(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchUsersState.update {
                    UiState.Loading()
                }
                delay(1_000L)
                val foundUsers = _userRepository.searchUsers(query)
                _searchUsersState.update {
                    UiState.Success(foundUsers)
                }
            } catch (e: Exception) {
                _searchUsersState.update {
                    UiState.Error(e.message)
                }
            }
        }
    }

    suspend fun getPopularIndoUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _popularIndoUsersState.update {
                    UiState.Loading()
                }
                delay(1_000L)
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
                    UiState.Success(detailUsers)
                }
            } catch (e: Exception) {
                _popularIndoUsersState.update {
                    UiState.Error(e.message)
                }
            }
        }
    }

    companion object {
        const val POPULAR_INDO_USERS_QUERY = "location:indonesia"
    }
}
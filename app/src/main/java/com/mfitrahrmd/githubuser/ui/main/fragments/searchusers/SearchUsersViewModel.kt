package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Status(val message: String) {
    class Idle(message: String) : Status(message)

    class Loading(message: String) : Status(message)

    class Error(message: String) : Status(message)

    class Success(message: String) : Status(message)
}

class UiState<T>(val status: Status, val data: T)

class SearchUsersViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private var _perPage: Int = 30
    private var _page: Int = 1
    private var _prev: Int? = null
    private var _next: Int? = null
    private var _first: Int? = null
    private var _last: Int? = null

    var username: String = ""

    private var _uiState: MutableStateFlow<UiState<List<User>?>> = MutableStateFlow(UiState(Status.Idle("idle"), null))
    val uiState: StateFlow<UiState<List<User>?>>
        get() = _uiState

    private var _searchUsersState: MutableStateFlow<BaseState<List<User>?>> =
        MutableStateFlow(BaseState.Success())
    val searchUsersState: StateFlow<BaseState<List<User>?>> = _searchUsersState

    private var _popularIndoUsersState: MutableStateFlow<BaseState<List<User>?>> = MutableStateFlow(
        BaseState.Success()
    )
    val popularIndoUsersState: StateFlow<BaseState<List<User>?>> = _popularIndoUsersState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPopularIndoUsers()
        }
    }

    suspend fun searchUsers() {
        if (username.isEmpty() || username.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update {
                    UiState(Status.Loading("loading"), it.data)
                }
                val foundUsers = _userRepository.searchUsers(username)
                _prev = foundUsers.previous
                _next = foundUsers.next
                _first = foundUsers.first
                _last = foundUsers.last
                _uiState.update {
                    UiState(Status.Success("success"), foundUsers.data)
                }
            } catch (e: Exception) {
                _uiState.update {
                    UiState(Status.Error("error"), null)
                }
            }
        }
    }

    suspend fun loadMoreSearchUsers() {
        if (_next == null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update {
                    UiState(Status.Loading("loading"), it.data)
                }
                val foundUsers = _userRepository.searchUsers(username, _next.toString())
                _prev = foundUsers.previous
                _next = foundUsers.next
                _first = foundUsers.first
                _last = foundUsers.last
                delay(3000L)
                _uiState.update {
                    if (foundUsers.data != null) {
                        return@update UiState(Status.Success("success"), it.data?.plus(foundUsers.data))
                    }
                    return@update UiState(Status.Success("success"), it.data)
                }
            } catch (e: Exception) {
                _uiState.update {
                    UiState(Status.Error("error"), null)
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
                val detailUsers = foundUsers.data?.map {
                    async {
                        _userRepository.findUserByUsername(it.login)
                    }
                }?.awaitAll()
                _popularIndoUsersState.update {
                    BaseState.Success(detailUsers?.filterNotNull())
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
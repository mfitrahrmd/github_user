package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(private val _userRepository: UserRepository) : ViewModel() {
    private var _perPage: Int = 30
    private var _page: Int = 1
    private var _prev: Int? = null
    private var _next: Int? = null
    private var _first: Int? = null
    private var _last: Int? = null

    var username: String = ""

    private var _searchUsersState: MutableStateFlow<BaseState<List<User>?>> = MutableStateFlow(BaseState.Idle())
    val searchUsersState: StateFlow<BaseState<List<User>?>>
        get() = _searchUsersState

    private var _popularIndoUsersState: MutableStateFlow<BaseState<List<User>?>> = MutableStateFlow(
        BaseState.Idle()
    )
    val popularIndoUsersState: StateFlow<BaseState<List<User>?>>
        get () = _popularIndoUsersState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPopularIndoUsers()
        }
    }

    suspend fun searchUsers() {
        if (username.isEmpty() || username.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchUsersState.update {
                    BaseState.Loading(null, null)
                }
                val foundUsers = _userRepository.searchUsers(username)
                _prev = foundUsers.previous
                _next = foundUsers.next
                _first = foundUsers.first
                _last = foundUsers.last
                _searchUsersState.update {
                    BaseState.Success(null, foundUsers.data)
                }
            } catch (e: Exception) {
                _searchUsersState.update {
                    BaseState.Error(e.message, null)
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
                _searchUsersState.update {
                    BaseState.Loading(null, it.data)
                }
                val foundUsers = _userRepository.searchUsers(username, _next.toString())
                _prev = foundUsers.previous
                _next = foundUsers.next
                _first = foundUsers.first
                _last = foundUsers.last
                _searchUsersState.update {
                    if (foundUsers.data != null) {
                        return@update BaseState.Success(null, it.data?.plus(foundUsers.data))
                    }
                    return@update BaseState.Success(null, it.data)
                }
            } catch (e: Exception) {
                _searchUsersState.update {
                    BaseState.Error(null, it.data)
                }
            }
        }
    }

    suspend fun getPopularIndoUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _popularIndoUsersState.update {
                    BaseState.Loading(null, null)
                }
                val foundUsers = _userRepository.searchUsers(POPULAR_INDO_USERS_QUERY)
                val detailUsers = foundUsers.data?.map {
                    async {
                        _userRepository.findUserByUsername(it.login)
                    }
                }?.awaitAll()
                _popularIndoUsersState.update {
                    BaseState.Success(null, detailUsers?.filterNotNull())
                }
            } catch (e: Exception) {
                _popularIndoUsersState.update {
                    BaseState.Error(e.message, null)
                }
            }
        }
    }

    companion object {
        const val POPULAR_INDO_USERS_QUERY = "location:indonesia"
    }
}
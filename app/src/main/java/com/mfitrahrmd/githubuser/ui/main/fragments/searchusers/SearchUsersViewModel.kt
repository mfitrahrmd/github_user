package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.Pagination
import com.mfitrahrmd.githubuser.repositories.UserDetailRepository
import com.mfitrahrmd.githubuser.repositories.UserSearchRepository
import com.mfitrahrmd.githubuser.repositories.UserPopularRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(
    private val _userSearchRepository: UserSearchRepository,
    private val _userDetailRepository: UserDetailRepository,
    private val _userPopularRepository: UserPopularRepository
) : ViewModel() {
    private var _searchUsersPagination: Pagination = Pagination(null, null, null, null)
    private var _popularUsersPagination: Pagination = Pagination(null, null, null, null)

    var username: String = ""

    private var _searchUsersState: MutableStateFlow<BaseState<List<User>?>> =
        MutableStateFlow(BaseState.Idle())
    val searchUsersState: StateFlow<BaseState<List<User>?>>
        get() = _searchUsersState

    private var _popularIndoUsersState: MutableStateFlow<BaseState<List<User>?>> = MutableStateFlow(
        BaseState.Idle()
    )
    val popularIndoUsersState: StateFlow<BaseState<List<User>?>>
        get() = _popularIndoUsersState

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
                val foundUsers = _userSearchRepository.searchUsers(username)
                _searchUsersPagination.apply {
                    previous = foundUsers.previous
                    next = foundUsers.next
                    first = foundUsers.first
                    last = foundUsers.last
                }
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
        if (_searchUsersPagination.next == null) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchUsersState.update {
                    BaseState.Loading(null, it.data)
                }
                val foundUsers = _userSearchRepository.searchUsers(username, _searchUsersPagination.next.toString())
                _searchUsersPagination.apply {
                    previous = foundUsers.previous
                    next = foundUsers.next
                    first = foundUsers.first
                    last = foundUsers.last
                }
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
                val foundUsers = _userPopularRepository.searchPopularUsers(POPULAR_USERS_LOCATION)
                _popularUsersPagination.apply {
                    previous = foundUsers.previous
                    next = foundUsers.next
                    first = foundUsers.first
                    last = foundUsers.last
                }
                val detailUsers = foundUsers.data?.map {
                    async {
                        _userDetailRepository.findUserByUsername(it.username)
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
        const val POPULAR_USERS_LOCATION = "indonesia"
    }
}
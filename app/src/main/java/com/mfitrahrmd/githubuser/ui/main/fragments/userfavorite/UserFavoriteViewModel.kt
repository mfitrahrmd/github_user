package com.mfitrahrmd.githubuser.ui.main.fragments.userfavorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.UserFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserFavoriteViewModel(
    private val _userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {
    private val _userFavorite: MutableStateFlow<BaseState<Flow<PagingData<User>>>> =
        MutableStateFlow(BaseState.Idle())
    val userFavorite: StateFlow<BaseState<Flow<PagingData<User>>>>
        get() = _userFavorite

    init {
        viewModelScope.launch {
            _userFavorite.value = BaseState.Loading(null, null)
            try {
                val favoritePage = _userFavoriteRepository.get().cachedIn(viewModelScope)
                _userFavorite.value = BaseState.Success(null, favoritePage)
            } catch (e: Exception) {
                BaseState.Error(e.message, _userFavorite.value.data)
            }
        }
    }
}
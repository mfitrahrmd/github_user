package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.repositories.DetailUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserFollowingViewModel(private val _detailUserRepository: DetailUserRepository) :
    ViewModel() {
    var username: String = ""
    private val _userFollowingState: MutableStateFlow<PagingData<User>> =
        MutableStateFlow(PagingData.empty())
    val userFollowingState: StateFlow<PagingData<User>>
        get() = _userFollowingState

    fun initData() {
        viewModelScope.launch {
            getFollowing()
        }
    }

    suspend fun getFollowing() {
        try {
            _detailUserRepository.getFollowing(username).cachedIn(viewModelScope).collect {
                _userFollowingState.value = it
            }
        } catch (e: Exception) {
            Log.d("USER NOT FOUND", e.message ?: "")
        }
    }
}
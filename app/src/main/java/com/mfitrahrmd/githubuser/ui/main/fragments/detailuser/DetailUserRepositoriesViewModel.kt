package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import androidx.lifecycle.ViewModel
import com.mfitrahrmd.githubuser.base.BaseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

sealed class Status {
    class Loading : Status()

    class Success : Status()

    class Idle : Status()

    class Error : Status()
}

data class UiState(val status: Status = Status.Idle(), val username: String = "", val repositoriesCount: Int = 0)

class DetailUserRepositoriesViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    fun setUsername(username: String) {
        _uiState.update {
            UiState(username = username)
        }
    }

    suspend fun getRepositoriesCount() {
        // TODO : get list repositories
        _uiState.update {
            it.copy(status = Status.Loading())
        }
        delay(3_000L)
        _uiState.update {
            it.copy(status = Status.Success(), repositoriesCount = 100)
        }
    }
}
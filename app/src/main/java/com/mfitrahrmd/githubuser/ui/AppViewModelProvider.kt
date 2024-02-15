package com.mfitrahrmd.githubuser.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mfitrahrmd.githubuser.GithubUserApplication
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.DetailUserViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchUsersViewModel(githubUserApplication().appContainer.userRepository)
        }
        initializer {
            DetailUserViewModel(githubUserApplication().appContainer.userRepository)
        }
    }

}

fun CreationExtras.githubUserApplication(): GithubUserApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GithubUserApplication)
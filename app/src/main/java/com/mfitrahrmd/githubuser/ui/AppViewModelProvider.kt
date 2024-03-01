package com.mfitrahrmd.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mfitrahrmd.githubuser.GithubUserApplication
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.DetailUserViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowersViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowingViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersViewModel

class EmptyViewModel : ViewModel()

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchUsersViewModel(githubUserApplication().appContainer.userSearchRepository, githubUserApplication().appContainer.userDetailRepository, githubUserApplication().appContainer.userPopularRepository)
        }
        initializer {
            DetailUserViewModel(githubUserApplication().appContainer.userDetailRepository)
        }
        initializer {
            EmptyViewModel()
        }
        initializer {
            UserFollowingViewModel(githubUserApplication().appContainer.userDetailRepository)
        }
        initializer {
            UserFollowersViewModel(githubUserApplication().appContainer.userDetailRepository)
        }
    }
}

fun CreationExtras.githubUserApplication(): GithubUserApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GithubUserApplication)
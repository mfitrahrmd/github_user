package com.mfitrahrmd.githubuser.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mfitrahrmd.githubuser.GithubUserApplication
import com.mfitrahrmd.githubuser.ui.main.EmptyViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.DetailUserViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowersViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowingViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.settings.SettingsViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.userfavorite.UserFavoriteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            SearchUsersViewModel(
                githubUserApplication().appContainer.searchUsersRepository,
                githubUserApplication().appContainer.popularUsersRepository,
                githubUserApplication().appContainer.userFavoriteRepository
            )
        }
        initializer {
            DetailUserViewModel(githubUserApplication().appContainer.detailUserRepository)
        }
        initializer {
            EmptyViewModel()
        }
        initializer {
            UserFollowingViewModel(githubUserApplication().appContainer.detailUserRepository, githubUserApplication().appContainer.userFavoriteRepository)
        }
        initializer {
            UserFollowersViewModel(githubUserApplication().appContainer.detailUserRepository, githubUserApplication().appContainer.userFavoriteRepository)
        }
        initializer {
            UserFavoriteViewModel(githubUserApplication().appContainer.userFavoriteRepository)
        }
        initializer {
            SettingsViewModel(githubUserApplication().appContainer.settingsRepository)
        }
    }
}

fun CreationExtras.githubUserApplication(): GithubUserApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GithubUserApplication)
package com.mfitrahrmd.githubuser.ui.main.fragments.main

import androidx.navigation.fragment.findNavController
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentMainBinding
import com.mfitrahrmd.githubuser.ui.main.EmptyViewModel
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import com.mfitrahrmd.githubuser.ui.main.fragments.userfavorite.UserFavoriteFragment

class MainFragment : BaseFragment<FragmentMainBinding, EmptyViewModel>(EmptyViewModel::class.java) {
    override fun bind() {
        with(viewBinding) {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.mainNavHostFragment, SearchUsersFragment())
                commit()
            }
            bnv.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.searchUsers -> {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.mainNavHostFragment, SearchUsersFragment())
                            commit()
                        }
                    }

                    R.id.userFavorite -> {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.mainNavHostFragment, UserFavoriteFragment())
                            commit()
                        }
                    }

                    R.id.settings -> {
                        findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
                    }
                }

                true
            }
        }
    }
}
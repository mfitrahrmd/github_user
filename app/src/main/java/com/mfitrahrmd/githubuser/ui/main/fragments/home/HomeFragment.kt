package com.mfitrahrmd.githubuser.ui.main.fragments.home

import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentHomeBinding
import com.mfitrahrmd.githubuser.ui.EmptyViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, EmptyViewModel>(EmptyViewModel::class.java) {
    override fun bind() {
        with(viewBinding) {
            btnSearchUsers.setOnClickListener {
                findNavController().navigate(
                    R.id.action_homeFragment_to_searchUsers
                )
            }
        }
    }
}
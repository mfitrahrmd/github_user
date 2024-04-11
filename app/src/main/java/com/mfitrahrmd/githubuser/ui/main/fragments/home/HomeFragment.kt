package com.mfitrahrmd.githubuser.ui.main.fragments.home

import androidx.navigation.fragment.findNavController
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentHomeBinding
import com.mfitrahrmd.githubuser.ui.main.EmptyViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, EmptyViewModel>(EmptyViewModel::class.java) {
    override fun bind() {
        with(viewBinding) {
            btnSearchUsers.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToMainFragment()
                )
            }
        }
    }
}
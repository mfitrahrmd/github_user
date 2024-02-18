package com.mfitrahrmd.githubuser.ui.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentHomeBinding
import com.mfitrahrmd.githubuser.ui.EmptyViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, EmptyViewModel>(EmptyViewModel::class.java) {
    private lateinit var _binding: FragmentHomeBinding

    override fun bind() {
        with(_binding) {
            btnSearchUsers.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchUsers, null, null, FragmentNavigatorExtras(
                    ivLogo to "ivLogo"
                ))
            }
        }
    }
}
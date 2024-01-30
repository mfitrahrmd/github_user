package com.mfitrahrmd.githubuser.ui.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding

    private fun bind() {
        with(_binding) {
            btnSearchUsers.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchUsers, null, null, FragmentNavigatorExtras(
                    ivLogo to "ivLogo"
                ))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
    }
}
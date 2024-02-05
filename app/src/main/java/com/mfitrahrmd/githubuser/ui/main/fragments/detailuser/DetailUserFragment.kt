package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.FragmentDetailUserBinding

class DetailUserFragment : Fragment() {
    private lateinit var _binding: FragmentDetailUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)

        return _binding.root
    }
}
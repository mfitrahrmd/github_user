package com.mfitrahrmd.githubuser.ui.main.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(SettingsViewModel::class.java) {
    override fun bind() {
        with(viewBinding) {
            swIsDarkTheme.setOnCheckedChangeListener { buttonView, isChecked ->
                lifecycleScope.launch {
                    viewModel.setIsDarkTheme(isChecked)
                }
            }
        }
    }

    override fun observe() {
        with(viewBinding) {
            lifecycleScope.launch {
                viewModel.getIsDarkTheme().collectLatest {
                    swIsDarkTheme.isChecked = it
                }
            }
        }
    }
}
package com.mfitrahrmd.githubuser.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.githubuser.GithubUserApplication
import com.mfitrahrmd.githubuser.databinding.ActivityMainBinding
import com.mfitrahrmd.githubuser.ui.AppViewModelProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private val _viewModel: MainViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        observe()
    }

    private fun observe() {
        val app = applicationContext as GithubUserApplication
        lifecycleScope.launch {
            app.appContainer.settingsRepository.getIsDarkTheme().collectLatest {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}
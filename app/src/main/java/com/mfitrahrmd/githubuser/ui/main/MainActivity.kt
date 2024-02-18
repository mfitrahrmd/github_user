package com.mfitrahrmd.githubuser.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mfitrahrmd.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}
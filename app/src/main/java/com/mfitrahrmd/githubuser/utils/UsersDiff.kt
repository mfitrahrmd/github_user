package com.mfitrahrmd.githubuser.utils

import androidx.recyclerview.widget.DiffUtil
import com.mfitrahrmd.githubuser.models.User

object UsersDiff : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return newItem == oldItem
    }
}
package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class SearchUsersAdapter(private val _onItemClick: (User) -> Unit) : PagingDataAdapter<User, ListUserAdapter.ListUserViewHolder>(UsersDiff) {
    override fun onBindViewHolder(holder: ListUserAdapter.ListUserViewHolder, position: Int) {
        holder.bind(getItem(position), _onItemClick)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserAdapter.ListUserViewHolder {
        return ListUserAdapter.ListUserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
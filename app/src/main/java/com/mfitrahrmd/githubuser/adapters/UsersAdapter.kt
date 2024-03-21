package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class UsersAdapter(private val _onItemClick: (User) -> Unit) :
    PagingDataAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiff) {
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position), _onItemClick)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersViewHolder {
        return UsersViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class UsersViewHolder(private val _binding: ItemUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(user: User?, onItemClickListener: ((User) -> Unit)?) {
            if (user != null) {
                with(user) {
                    with(_binding) {
                        tvUsername.text = itemView.context.getString(R.string.username, username)
                        Glide.with(_binding.root)
                            .load(avatarUrl)
                            .into(ivAvatar)
                        this@UsersViewHolder.itemView.setOnClickListener {
                            onItemClickListener?.let { it1 -> it1(user) }
                        }
                    }
                }
            }
        }
    }
}
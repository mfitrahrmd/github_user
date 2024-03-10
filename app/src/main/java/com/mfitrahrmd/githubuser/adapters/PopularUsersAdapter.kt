package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.databinding.ItemPopularIndoUsersBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class PopularUsersAdapter(private val _onItemClick: (User) -> Unit) :
    PagingDataAdapter<User, PopularUsersAdapter.PopularUsersViewHolder>(UsersDiff) {
    class PopularUsersViewHolder(private val _binding: ItemPopularIndoUsersBinding) :
        RecyclerView.ViewHolder(_binding.root) {
            fun bind(user: User?, _onItemClick: (User) -> Unit) {
                with(_binding) {
                    if (user != null) {
                        tvName.text = user.name
                        tvUsername.text = user.username
                        tvBio.text = user.bio
                        Glide.with(_binding.root)
                            .load(user.avatarUrl)
                            .into(ivAvatar)
                        this@PopularUsersViewHolder.itemView.setOnClickListener {
                            _onItemClick(user)
                        }
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularUsersViewHolder {
        return PopularUsersViewHolder(
            ItemPopularIndoUsersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularUsersViewHolder, position: Int) {
        holder.bind(getItem(position), _onItemClick)
    }
}

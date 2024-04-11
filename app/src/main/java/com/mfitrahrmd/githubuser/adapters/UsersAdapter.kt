package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class UsersAdapter(
    private val _onItemClick: (User) -> Unit,
    private val _onFavoriteClick: (User) -> Unit
) :
    PagingDataAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiff) {
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position), _onItemClick, _onFavoriteClick)
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
        fun bind(
            user: User?,
            onItemClickListener: ((User) -> Unit)?,
            onFavoriteClick: (User) -> Unit
        ) {
            if (user != null) {
                with(user) {
                    with(_binding) {
                        tvUsername.text = itemView.context.getString(R.string.username, username)
                        if (user.favorite.`is`) {
                            ivIsFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    ivIsFavorite.context,
                                    R.drawable.heart_24
                                )
                            )
                        } else {
                            ivIsFavorite.setImageDrawable(
                                ContextCompat.getDrawable(
                                    ivIsFavorite.context,
                                    R.drawable.heart_outlined_24
                                )
                            )
                        }
                        ivIsFavorite.setOnClickListener {
                            onFavoriteClick(user)
                        }
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
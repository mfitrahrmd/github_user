package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemPopularUsersBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class PopularUsersAdapter(
    private val _onItemClick: (User) -> Unit,
    private val _onFavoriteClick: (User) -> Unit
) :
    PagingDataAdapter<User, PopularUsersAdapter.PopularUsersViewHolder>(UsersDiff) {
    class PopularUsersViewHolder(private val _binding: ItemPopularUsersBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(user: User?, onItemClick: (User) -> Unit, onFavoriteClick: (User) -> Unit) {
            with(_binding) {
                if (user != null) {
                    tvName.text = user.name
                    tvUsername.text = user.username
                    tvBio.text = user.bio
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
                        .load(user.avatarUrl)
                        .into(ivAvatar)
                    this@PopularUsersViewHolder.itemView.setOnClickListener {
                        onItemClick(user)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularUsersViewHolder {
        return PopularUsersViewHolder(
            ItemPopularUsersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularUsersViewHolder, position: Int) {
        holder.bind(getItem(position), _onItemClick, _onFavoriteClick)
    }
}

package com.mfitrahrmd.githubuser.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.models.User

class SearchUsersAdapter(private var _users: List<User>, private val _onItemClick: (User) -> Unit) :
    RecyclerView.Adapter<SearchUsersAdapter.SearchUsersViewHolder>() {
    class SearchUsersViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = _users.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {
        with(holder.binding) {
            with(_users[position]) {
                tvUsername.text = holder.itemView.context.getString(R.string.username, login)
                Glide.with(holder.binding.root)
                    .load(this.avatarUrl)
                    .into(ivAvatar)
                root.setOnClickListener {
                    _onItemClick(this)
                }
            }
        }
    }

    fun setUsers(setter: (currentUsers: List<User>) -> List<User>) {
        _users = setter(_users)
        notifyDataSetChanged()
    }
}
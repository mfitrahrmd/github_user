package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.githubuser.databinding.ItemSearchUsersBinding
import com.mfitrahrmd.githubuser.models.User

class SearchUsersAdapter(private var _users: List<User>) :
    RecyclerView.Adapter<SearchUsersAdapter.SearchUsersViewHolder>() {
    class SearchUsersViewHolder(val binding: ItemSearchUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        return SearchUsersViewHolder(
            ItemSearchUsersBinding.inflate(
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
                tvName.text = name ?: "-"
                tvUsername.text = login.ifBlank { "-" }
            }
        }
    }

    fun setUsers(setter: (currentUsers: List<User>) -> List<User>) {
        _users = setter(_users)
        notifyDataSetChanged()
    }
}
package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.entities.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class ListUserAdapter(private var _users: List<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>() {
    private val _usersDiffer = AsyncListDiffer<User>(this, UsersDiff)
    val users: List<User>
        get() = _usersDiffer.currentList
    private var _onItemClickListener: ((User) -> Unit)? = null

    class ListUserViewHolder(private val _binding: ItemUserBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun bind(holder: ListUserViewHolder, user: User, onItemClickListener: ((User) -> Unit)?) {
            with(user) {
                with(_binding) {
                    tvUsername.text = itemView.context.getString(R.string.username, username)
                    Glide.with(_binding.root)
                        .load(avatarUrl)
                        .into(ivAvatar)
                    holder.itemView.setOnClickListener {
                        onItemClickListener?.let { it1 -> it1(user) }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        return ListUserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = _usersDiffer.currentList.size

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.bind(holder, _usersDiffer.currentList[position], _onItemClickListener)
    }

    fun setOnItemClickListener(onItemClick: (User) -> Unit) {
        _onItemClickListener = onItemClick
    }

    fun setUsers(setter: (List<User>) -> List<User>) {
        val newUsers = setter(_usersDiffer.currentList)
        _usersDiffer.submitList(newUsers)
    }
}
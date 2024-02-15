package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.ItemUserBinding
import com.mfitrahrmd.githubuser.models.User

class ListUserAdapter(private var _users: List<User>) : RecyclerView.Adapter<ListUserAdapter.ListUserViewHolder>() {
    private lateinit var _binding: ItemUserBinding

    class ListUserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        _binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListUserViewHolder(_binding)
    }

    override fun getItemCount(): Int = _users.size

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        with(_binding) {
            with(_users[position]) {
                tvUsername.text = holder.itemView.context.getString(R.string.username, login)
                Glide.with(holder.binding.root)
                    .load(this.avatarUrl)
                    .into(ivAvatar)
            }
        }
    }

    fun setData(setter: (List<User>) -> List<User>) {
        _users = setter(_users)
        notifyDataSetChanged()
    }
}
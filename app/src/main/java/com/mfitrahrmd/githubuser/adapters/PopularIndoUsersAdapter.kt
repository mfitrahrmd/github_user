package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.githubuser.databinding.ItemPopularIndoUsersBinding
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.utils.UsersDiff

class PopularIndoUsersAdapter(private var _popularIndoUsers: List<User>) :
    RecyclerView.Adapter<PopularIndoUsersAdapter.PopularIndoUsersViewHolder>() {
    private val _popularIndoUsersDiff = AsyncListDiffer<User>(this, UsersDiff)
    private var _onItemClickListener: ((User) -> Unit)? = null

    class PopularIndoUsersViewHolder(val binding: ItemPopularIndoUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularIndoUsersViewHolder {
        return PopularIndoUsersViewHolder(
            ItemPopularIndoUsersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = _popularIndoUsersDiff.currentList.size

    override fun onBindViewHolder(holder: PopularIndoUsersViewHolder, position: Int) {
        with(holder.binding) {
            with(_popularIndoUsersDiff.currentList[position]) {
                tvName.text = this.name
                tvUsername.text = this.login
                tvBio.text = this.bio
                Glide.with(holder.binding.root)
                    .load(this.avatarUrl)
                    .into(ivAvatar)
                holder.itemView.setOnClickListener {
                    _onItemClickListener?.let { it1 -> it1(this) }
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClick: (User) -> Unit) {
        _onItemClickListener = onItemClick
    }

    fun setPopularIndoUsers(setter: (currentPopularIndoUsers: List<User>) -> List<User>) {
        val newPopularIndoUsers = setter(_popularIndoUsersDiff.currentList)
        _popularIndoUsersDiff.submitList(newPopularIndoUsers)
    }
}

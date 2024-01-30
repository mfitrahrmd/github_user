package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.githubuser.databinding.ItemPopularIndoUsersBinding
import com.mfitrahrmd.githubuser.models.User

class PopularIndoUsersAdapter(private var _popularIndoUsers: List<User>) :
    RecyclerView.Adapter<PopularIndoUsersAdapter.PopularIndoUsersViewHolder>() {
    class PopularIndoUsersViewHolder(val binding: ItemPopularIndoUsersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularIndoUsersViewHolder {
        return PopularIndoUsersViewHolder(ItemPopularIndoUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = _popularIndoUsers.size

    override fun onBindViewHolder(holder: PopularIndoUsersViewHolder, position: Int) {
        with(holder.binding) {
            with(_popularIndoUsers[position]) {
                tvName.text = this.name
                tvUsername.text = this.login
            }
        }
    }

    fun setPopularIndoUsers(setter: (currentPopularIndoUsers: List<User>) -> List<User>) {
        _popularIndoUsers = setter(_popularIndoUsers)
        notifyDataSetChanged()
    }
}

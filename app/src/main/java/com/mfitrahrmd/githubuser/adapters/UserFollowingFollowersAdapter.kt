package com.mfitrahrmd.githubuser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowFragment
import kotlinx.coroutines.flow.StateFlow

class UserFollowingFollowersAdapter(
    private val _dataFlow: DataFlow,
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    interface DataFlow {
        val userFollowingState: StateFlow<BaseState<List<User>>>
        val userFollowersState: StateFlow<BaseState<List<User>>>
    }

    val pages = listOf(
        Page("Following", UserFollowFragment(_dataFlow.userFollowingState)),
        Page("Followers", UserFollowFragment(_dataFlow.userFollowersState)),
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return pages[position].fragment
    }

    data class Page(
        val title: String, // title used by tab layout
        val fragment: Fragment
    )
}

package com.mfitrahrmd.githubuser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.ui.main.fragments.detailuser.UserFollowFragment

class UserFollowingFollowersAdapter(
    private val _following: List<User>,
    private val _followers: List<User>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val _userFollowingPage = UserFollowFragment(_following)
    private val _userFollowersPage = UserFollowFragment(_followers)
    val pages = arrayOf(
        Page("Following", _userFollowingPage),
        Page("Followers", _userFollowersPage),
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return pages[position].fragment
    }

    fun setFollowingData(setter: (List<User>) -> List<User>) {
        _userFollowingPage.setData(setter)
    }

    fun setFollowersData(setter: (List<User>) -> List<User>) {
        _userFollowersPage.setData(setter)
    }

    data class Page(
        val title: String, // title used by tab layout
        val fragment: Fragment
    )
}

package com.mfitrahrmd.githubuser.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserFollowingFollowersAdapter(
    private val _pages: List<Page>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = _pages.size

    override fun createFragment(position: Int): Fragment {
        return _pages[position].fragment
    }

    class Page(
        val title: String, // title used by tab layout
        val fragment: Fragment
    )
}

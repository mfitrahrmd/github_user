package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.ListUserAdapter
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.models.User

class UserFollowFragment(private val _data: List<User>) : Fragment() {
    private lateinit var _binding: FragmentUserFollowBinding
    private val _listUserFollowAdapter = ListUserAdapter(_data)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
    }

    private fun bind() {
        with(_binding) {
            rvFollow.layoutManager = LinearLayoutManager(this@UserFollowFragment.context, LinearLayoutManager.VERTICAL, false)
            rvFollow.adapter = _listUserFollowAdapter
        }
    }

    fun setData(setter: (List<User>) -> List<User>) {
        _listUserFollowAdapter.setData(setter)
    }
}
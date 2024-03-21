package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFollowingFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowingViewModel>(UserFollowingViewModel::class.java) {
    private val _listUserFollowingAdapter: UsersAdapter = UsersAdapter {
        findNavController().navigate(
            DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                it.username
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.username = UserFollowingFragmentArgs.fromBundle(arguments as Bundle).username
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.initData()
        }
    }

    override fun onResume() {
        super.onResume()
        viewBinding.root.requestLayout()
    }

    override fun bind() {
        with(viewBinding) {
            rvFollow.apply {
                layoutManager = LinearLayoutManager(
                    this@UserFollowingFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFollowingAdapter
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowingState.collectLatest {
                    _listUserFollowingAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userFollowingFragmentArgs: UserFollowingFragmentArgs) =
            UserFollowingFragment().apply {
                arguments = userFollowingFragmentArgs.toBundle()
            }
    }
}
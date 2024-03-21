package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.LoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFollowersFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowersViewModel>(UserFollowersViewModel::class.java) {
    private val _listUserFollowersAdapter: UsersAdapter = UsersAdapter {
        findNavController().navigate(
            DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                it.username
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUsername(UserFollowersFragmentArgs.fromBundle(arguments as Bundle).username)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.root.requestLayout()
    }

    override fun bind() {
        with(viewBinding) {
            rvFollow.apply {
                layoutManager = LinearLayoutManager(
                    this@UserFollowersFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFollowersAdapter.withLoadStateFooter(LoaderStateAdapter {})
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowers.collectLatest {
                    _listUserFollowersAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userFollowersFragmentArgs: UserFollowersFragmentArgs) =
            UserFollowersFragment().apply {
                arguments = userFollowersFragmentArgs.toBundle()
            }
    }
}
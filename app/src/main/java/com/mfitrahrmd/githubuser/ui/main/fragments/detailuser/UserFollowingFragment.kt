package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.UsersLoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import kotlinx.coroutines.launch

class UserFollowingFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowingViewModel>(UserFollowingViewModel::class.java) {
    private val _listUserFollowingAdapter: UsersAdapter = UsersAdapter({
        findNavController().navigate(
            DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                it.username
            )
        )
    }, {
        if (it.favorite.`is`) {
            viewModel.removeFromFavorite(it)
        } else {
            viewModel.addToFavorite(it)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUsername(UserFollowingFragmentArgs.fromBundle(arguments as Bundle).username)
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
                adapter = _listUserFollowingAdapter.withLoadStateFooter(UsersLoaderStateAdapter {})
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowing.collect {
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

        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
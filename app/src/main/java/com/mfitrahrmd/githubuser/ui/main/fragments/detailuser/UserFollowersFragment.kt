package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.adapters.UsersLoaderStateAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import kotlinx.coroutines.launch

class UserFollowersFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowersViewModel>(UserFollowersViewModel::class.java) {
    private val _listUserFollowersAdapter: UsersAdapter = UsersAdapter({
        findNavController().navigate(
            DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                it.username
            )
        )
    }, {
        if (it.favorite.`is`) {
            mainViewModel.removeFromFavorite(it)
        } else {
            mainViewModel.addToFavorite(it)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUsername(UserFollowersFragmentArgs.fromBundle(arguments as Bundle).username)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.root.requestLayout()
        _listUserFollowersAdapter.addLoadStateListener(::loadStateListener)
    }

    override fun onPause() {
        super.onPause()
        _listUserFollowersAdapter.removeLoadStateListener(::loadStateListener)
    }

    override fun bind() {
        with(viewBinding) {
            rvFollow.apply {
                layoutManager = LinearLayoutManager(
                    this@UserFollowersFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFollowersAdapter.withLoadStateFooter(UsersLoaderStateAdapter {})
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowers.collect {
                    _listUserFollowersAdapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun loadStateListener(combinedLoadState: CombinedLoadStates): Unit {
        if (combinedLoadState.refresh is LoadState.Loading) {
            with(viewBinding) {
                shimmer.apply {
                    startShimmer()
                    visibility = View.VISIBLE
                }
                rvFollow.apply {
                    visibility = View.GONE
                }
            }
        } else {
            with(viewBinding) {
                shimmer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
                rvFollow.apply {
                    visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args: UserFollowersFragmentArgs) =
            UserFollowersFragment().apply {
                arguments = args.toBundle()
            }

        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
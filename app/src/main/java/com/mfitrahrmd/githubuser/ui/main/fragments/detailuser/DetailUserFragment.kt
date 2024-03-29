package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.adapters.UserFollowingFollowersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentDetailUserBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.launch

private const val ARG_USERNAME = "username"

class DetailUserFragment :
    BaseFragment<FragmentDetailUserBinding, DetailUserViewModel>(DetailUserViewModel::class.java) {
    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailUser.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                shimmerDetailUser.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                tvName.text = currentUiState.data?.name
                                tvUsername.text = this@DetailUserFragment.getString(
                                    R.string.username,
                                    currentUiState.data?.username
                                )
                                tvFollowingCount.text = this@DetailUserFragment.getString(
                                    R.string.followingCount,
                                    currentUiState.data?.following?.count
                                )
                                tvFollowersCount.text = this@DetailUserFragment.getString(
                                    R.string.followersCount,
                                    currentUiState.data?.followers?.count
                                )
                                tvBio.text = currentUiState.data?.bio
                                Glide.with(this@DetailUserFragment)
                                    .load(currentUiState.data?.avatarUrl)
                                    .into(ivAvatar)
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                shimmerDetailUser.apply {
                                    startShimmer()
                                    visibility = View.VISIBLE
                                }
                            }
                        }

                        is BaseState.Error -> {
                            with(viewBinding) {
                                shimmerDetailUser.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                            }
                            Toast.makeText(
                                view?.context,
                                if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else SearchUsersFragment.DEFAULT_ERROR_MESSAGE,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUsername(DetailUserFragmentArgs.fromBundle(arguments as Bundle).username)
        lifecycleScope.launch {
            viewModel.getDetailUser()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val pages: List<UserFollowingFollowersAdapter.Page> = mutableListOf(
                    UserFollowingFollowersAdapter.Page(
                        "Following",
                        UserFollowingFragment.newInstance(
                            UserFollowingFragmentArgs.Builder(viewModel.username).build()
                        )
                    ),
                    UserFollowingFollowersAdapter.Page(
                        "Followers",
                        UserFollowersFragment.newInstance(
                            UserFollowersFragmentArgs.Builder(viewModel.username).build()
                        )
                    )
                )
                with(viewBinding) {
                    vpFollowingFollowers.adapter = UserFollowingFollowersAdapter(pages, childFragmentManager, lifecycle)
                    TabLayoutMediator(tlFollowingFollowers, vpFollowingFollowers) { tab, position ->
                        tab.text = pages[position].title
                    }.attach()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args: DetailUserFragmentArgs) = DetailUserFragment().apply {
            arguments = args.toBundle()
        }
    }
}
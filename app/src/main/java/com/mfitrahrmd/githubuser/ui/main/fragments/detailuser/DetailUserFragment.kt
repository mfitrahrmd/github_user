package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch

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
                                if (currentUiState.data == null) return@collect
                                fabIsFavorite.setOnClickListener {
                                    if (currentUiState.data.favorite.`is`) {
                                        mainViewModel.removeFromFavorite(currentUiState.data)
                                    } else {
                                        mainViewModel.addToFavorite(currentUiState.data)
                                    }
                                }
                                if (currentUiState.data.favorite.`is`) {
                                    fabIsFavorite.setImageResource(R.drawable.heart_24)
                                } else {
                                    fabIsFavorite.setImageResource(R.drawable.heart_outlined_24)
                                }
                                tvName.text = currentUiState.data.name
                                tvUsername.text = this@DetailUserFragment.getString(
                                    R.string.username, currentUiState.data.username
                                )
                                tvBio.text = currentUiState.data.bio
                                tvFollowingCount.text = this@DetailUserFragment.getString(
                                    R.string.followingCount, currentUiState.data.following.count
                                )
                                tvFollowersCount.text = this@DetailUserFragment.getString(
                                    R.string.followersCount, currentUiState.data.followers.count
                                )
                                if (currentUiState.data.company.isNullOrEmpty()) {
                                    tvCompany.visibility = View.GONE
                                } else {
                                    tvCompany.text = currentUiState.data.company
                                }
                                if (currentUiState.data.location.isNullOrEmpty()) {
                                    tvLocation.visibility = View.GONE
                                } else {
                                    tvLocation.text = currentUiState.data.location
                                }
                                if (currentUiState.data.twitterUsername.isNullOrEmpty()) {
                                    tvTwitter.visibility = View.GONE
                                } else {
                                    tvTwitter.text = currentUiState.data.twitterUsername
                                }
                                Glide.with(this@DetailUserFragment)
                                    .load(currentUiState.data.avatarUrl).into(ivAvatar)
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
                                if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val pages: List<UserFollowingFollowersAdapter.Page> = mutableListOf(
                UserFollowingFollowersAdapter.Page(
                    "Following", UserFollowingFragment.newInstance(
                        UserFollowingFragmentArgs.Builder(viewModel.username.value).build()
                    )
                ), UserFollowingFollowersAdapter.Page(
                    "Followers", UserFollowersFragment.newInstance(
                        UserFollowersFragmentArgs.Builder(viewModel.username.value).build()
                    )
                )
            )
            with(viewBinding) {
                vpFollowingFollowers.adapter =
                    UserFollowingFollowersAdapter(pages, childFragmentManager, lifecycle)
                TabLayoutMediator(tlFollowingFollowers, vpFollowingFollowers) { tab, position ->
                    tab.text = pages[position].title
                }.attach()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args: DetailUserFragmentArgs) = DetailUserFragment().apply {
            arguments = args.toBundle()
        }

        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.adapters.UserFollowingFollowersAdapter
import com.mfitrahrmd.githubuser.databinding.FragmentDetailUserBinding
import com.mfitrahrmd.githubuser.ui.AppViewModelProvider
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.launch

class DetailUserFragment : Fragment() {
    private lateinit var _binding: FragmentDetailUserBinding
    private val _viewModel: DetailUserViewModel by viewModels(factoryProducer = { AppViewModelProvider.Factory })
    private lateinit var _userFollowingFollowersAdapter: UserFollowingFollowersAdapter

    private fun bind(userFollowingFollowersAdapter: UserFollowingFollowersAdapter) {
        with(_binding) {
            vpFollowingFollowers.adapter = userFollowingFollowersAdapter
            TabLayoutMediator(tlFollowingFollowers, vpFollowingFollowers) { tab, position ->
                tab.text = userFollowingFollowersAdapter.pages[position].title
            }.attach()
        }
    }

    private fun observe(userFollowingFollowersAdapter: UserFollowingFollowersAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.userState.collect { currentUiState ->
                    when(currentUiState) {
                        is BaseState.Success -> {
                            with(_binding) {
                                shimmerDetailUser.stopShimmer()
                                shimmerDetailUser.visibility = View.GONE

                                tvName.text = currentUiState.data?.name
                                tvUsername.text = this@DetailUserFragment.getString(R.string.username, currentUiState.data?.login)
                                tvFollowingCount.text = this@DetailUserFragment.getString(R.string.followingCount, currentUiState.data?.following)
                                tvFollowersCount.text = this@DetailUserFragment.getString(R.string.followersCount, currentUiState.data?.followers)
                                tvBio.text = currentUiState.data?.bio
                                Glide.with(this@DetailUserFragment)
                                    .load(currentUiState.data?.avatarUrl)
                                    .into(ivAvatar)
                            }
                        }

                        is BaseState.Loading-> {
                            with(_binding) {
                                shimmerDetailUser.startShimmer()
                                shimmerDetailUser.visibility = View.VISIBLE
                            }
                        }

                        is BaseState.Error -> {
                            Toast.makeText(
                                view?.context,
                                if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else SearchUsersFragment.DEFAULT_ERROR_MESSAGE,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)

        Log.d("VIEWMODEL", _viewModel.toString())

        _userFollowingFollowersAdapter = UserFollowingFollowersAdapter(_viewModel, requireActivity())

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = DetailUserFragmentArgs.fromBundle(arguments as Bundle).username

        bind(_userFollowingFollowersAdapter)
        observe(_userFollowingFollowersAdapter)

        lifecycleScope.launch {
            launch {
                _viewModel.getUser(username)
            }
            launch {
                _viewModel.getListFollowing(username)
            }
            launch {
                _viewModel.getListFollowers(username)
            }
        }
    }
}
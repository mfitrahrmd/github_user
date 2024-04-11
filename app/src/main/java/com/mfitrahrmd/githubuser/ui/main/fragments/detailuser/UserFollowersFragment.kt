package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.LoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.flow.collectLatest
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
            viewModel.removeFromFavorite(it)
        } else {
            viewModel.addToFavorite(it)
        }
    })

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
                viewModel.userFollowers.collectLatest { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                shimmer.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                currentUiState.data?.collectLatest {
                                    _listUserFollowersAdapter.submitData(lifecycle, it)
                                }
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                shimmer.apply {
                                    startShimmer()
                                    visibility = View.VISIBLE
                                }
                            }
                        }

                        is BaseState.Error -> {
                            with(viewBinding) {
                                shimmer.apply {
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

    companion object {
        @JvmStatic
        fun newInstance(args: UserFollowersFragmentArgs) =
            UserFollowersFragment().apply {
                arguments = args.toBundle()
            }

        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
package com.mfitrahrmd.githubuser.ui.main.fragments.userfavorite

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
import com.mfitrahrmd.githubuser.databinding.FragmentUserFavoriteBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val ARG_USERNAME = "username"

class UserFavoriteFragment :
    BaseFragment<FragmentUserFavoriteBinding, UserFavoriteViewModel>(UserFavoriteViewModel::class.java) {
    private val _listUserFavoriteAdapter: UsersAdapter = UsersAdapter {
        findNavController().navigate(
            UserFavoriteFragmentDirections.actionUserFavoriteFragmentToDetailUserFragment(
                it.username
            )
        )
    }

    override fun bind() {
        with(viewBinding) {
            rvUserFavorite.apply {
                layoutManager = LinearLayoutManager(
                    this@UserFavoriteFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFavoriteAdapter.withLoadStateFooter(LoaderStateAdapter {})
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFavorite.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                shimmer.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                currentUiState.data?.collectLatest {
                                    _listUserFavoriteAdapter.submitData(lifecycle, it)
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

    companion object {
        @JvmStatic
        fun newInstance() = UserFavoriteFragment()
    }
}
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
import com.mfitrahrmd.githubuser.ui.main.fragments.main.MainFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFavoriteFragment :
    BaseFragment<FragmentUserFavoriteBinding, UserFavoriteViewModel>(UserFavoriteViewModel::class.java) {
    private val _listUserFavoriteAdapter: UsersAdapter = UsersAdapter({
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailUserFragment(it.username)        )
    }, {
        if (it.favorite.`is`) {
            viewModel.removeFromFavorite(it)
        } else {
            viewModel.addToFavorite(it)
        }
    })

    override fun bind() {
        with(viewBinding) {
            rvUserFavorite.apply {
                layoutManager = LinearLayoutManager(
                    this@UserFavoriteFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFavoriteAdapter.withLoadStateFooter(LoaderStateAdapter {/*TODO*/})
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
        fun newInstance() = UserFavoriteFragment()

        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
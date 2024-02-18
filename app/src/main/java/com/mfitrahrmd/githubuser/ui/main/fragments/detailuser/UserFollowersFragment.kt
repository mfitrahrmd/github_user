package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.ListUserAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.launch

private const val ARG_USERNAME = "username"

class UserFollowersFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowersViewModel>(UserFollowersViewModel::class.java) {
    private var username: String? = null
    private lateinit var _listUserFollowAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
        _listUserFollowAdapter = ListUserAdapter(emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username?.let {
            lifecycleScope.launch {
                launch {
                    viewModel.getListFollowers(it)
                }
            }
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
                    this@UserFollowersFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFollowAdapter
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowersState.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                shimmerFollow.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                            }
                            _listUserFollowAdapter.apply {
                                setUsers {
                                    currentUiState.data ?: it
                                }
                                notifyDataSetChanged()
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                shimmerFollow.apply {
                                    startShimmer()
                                    visibility = View.VISIBLE
                                }
                            }
                        }

                        is BaseState.Error -> {
                            with(viewBinding) {
                                shimmerFollow.apply {
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
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            UserFollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }
}
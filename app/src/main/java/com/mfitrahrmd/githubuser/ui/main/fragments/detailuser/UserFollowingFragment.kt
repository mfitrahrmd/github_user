package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.ListUserAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.launch

class UserFollowingFragment :
    BaseFragment<FragmentUserFollowBinding, UserFollowingViewModel>(UserFollowingViewModel::class.java) {
    private lateinit var username: String
    private val _listUserFollowingAdapter: ListUserAdapter = ListUserAdapter(emptyList()).apply {
        setOnItemClickListener {
            findNavController().navigate(
                DetailUserFragmentDirections.actionDetailUserFragmentSelf(
                    it.username
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = UserFollowingFragmentArgs.fromBundle(arguments as Bundle).username
        viewModel.username = username
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.initData()
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
                    this@UserFollowingFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _listUserFollowingAdapter
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFollowingState.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                shimmerFollow.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                            }
                            _listUserFollowingAdapter.apply {
                                setUsers {
                                    currentUiState.data ?: it
                                }
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

                        else -> {}
                    }
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
    }
}
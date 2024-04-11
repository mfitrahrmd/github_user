package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.LoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.PopularUsersAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.main.MainFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchUsersFragment :
    BaseFragment<FragmentSearchUsersBinding, SearchUsersViewModel>(SearchUsersViewModel::class.java) {
    private val _searchUsersAdapter: UsersAdapter = UsersAdapter({
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailUserFragment(it.username)
        )
    }, {
        if (it.favorite.`is`) {
            viewModel.removeFromFavorite(it)
        } else {
            viewModel.addToFavorite(it)
        }
    })

    private val _popularUsersAdapter: PopularUsersAdapter = PopularUsersAdapter({
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailUserFragment(it.username)
        )
    }, {
        if (it.favorite.`is`) {
            viewModel.removeFromFavorite(it)
        } else {
            viewModel.addToFavorite(it)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPopularUsers()
    }

    override fun bind() {
        with(viewBinding) {
            /*
            * Setup search view
            * */
            searchView.apply {
                setupWithSearchBar(searchBar)
                editText
                    .setOnEditorActionListener { _, _, _ ->
                        searchBar.setText(searchView.text)
                        searchView.hide()
                        lifecycleScope.launch {
                            viewModel.searchUsers(searchView.text.toString())
                        }

                        true
                    }
            }
            rvSearchUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                )
                adapter = _searchUsersAdapter.withLoadStateFooter(LoaderStateAdapter {})
            }
            rvPopularUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = _popularUsersAdapter
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchUsersState.collectLatest { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            with(viewBinding) {
                                tvTitleSearchResult.visibility = View.VISIBLE
                                rvSearchUsers.apply {
                                    visibility = View.VISIBLE
                                }
                                currentUiState.data?.collectLatest {
                                    _searchUsersAdapter.submitData(lifecycle, it)
                                }
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                rvSearchUsers.apply {
                                    visibility = View.GONE
                                }
                            }
                        }

                        is BaseState.Error -> {
                            Toast.makeText(
                                view?.context,
                                if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is BaseState.Idle -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.popularUsersState.collectLatest { currentUiState ->
                when (currentUiState) {
                    is BaseState.Success -> {
                        with(viewBinding) {
                            tvTitlePopularUsers.visibility = View.VISIBLE
                            rvPopularUsers.apply {
                                visibility = View.VISIBLE
                            }
                            currentUiState.data?.collectLatest {
                                _popularUsersAdapter.submitData(lifecycle, it)
                            }
                        }
                    }

                    is BaseState.Loading -> {
                        with(viewBinding) {
                            tvTitlePopularUsers.visibility = View.GONE
                            rvPopularUsers.apply {
                                visibility = View.GONE
                            }
                        }
                    }

                    is BaseState.Error -> {
                        Toast.makeText(
                            view?.context,
                            if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is BaseState.Idle -> {}
                }
            }
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
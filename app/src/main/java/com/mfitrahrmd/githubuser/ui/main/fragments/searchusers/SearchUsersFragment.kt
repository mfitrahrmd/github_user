package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.mfitrahrmd.githubuser.adapters.LoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.PopularUsersAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchUsersFragment :
    BaseFragment<FragmentSearchUsersBinding, SearchUsersViewModel>(SearchUsersViewModel::class.java) {
    private val _searchUsersAdapter: UsersAdapter = UsersAdapter {
        findNavController().navigate(
            SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(
                it.username
            )
        )
    }

    private val _popularUsersAdapter: PopularUsersAdapter = PopularUsersAdapter({
        findNavController().navigate(
            SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(
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
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

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
                            Log.d("BEGIN SEARCH", searchView.text.toString())
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
            rvPopularIndoUsers.apply {
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
            viewModel.searchUsersState.collectLatest {
                _searchUsersAdapter.submitData(lifecycle, it)
            }
        }
        lifecycleScope.launch {
            viewModel.popularUsersState.collectLatest {
                _popularUsersAdapter.submitData(lifecycle, it)
            }
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
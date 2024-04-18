package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.mfitrahrmd.githubuser.adapters.UsersLoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.PopularUsersAdapter
import com.mfitrahrmd.githubuser.adapters.PopularUsersLoaderStateAdapter
import com.mfitrahrmd.githubuser.adapters.UsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.base.BaseState
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import com.mfitrahrmd.githubuser.ui.main.fragments.main.MainFragmentDirections
import kotlinx.coroutines.flow.collect
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
            mainViewModel.removeFromFavorite(it)
        } else {
            mainViewModel.addToFavorite(it)
        }
    })

    private val _popularUsersAdapter: PopularUsersAdapter = PopularUsersAdapter({
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailUserFragment(it.username)
        )
    }, {
        if (it.favorite.`is`) {
            mainViewModel.removeFromFavorite(it)
        } else {
            mainViewModel.addToFavorite(it)
        }
    })

    override fun onResume() {
        super.onResume()
        with(viewBinding) {
            _searchUsersAdapter.addLoadStateListener {
                loadStateListener(it, shimmerSearchUsers, rvSearchUsers)
            }
            _popularUsersAdapter.addLoadStateListener {
                loadStateListener(it, shimmerPopularUsers, rvPopularUsers)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        with(viewBinding) {
            _searchUsersAdapter.removeLoadStateListener {
                loadStateListener(it, shimmerSearchUsers, rvSearchUsers)
            }
            _popularUsersAdapter.removeLoadStateListener {
                loadStateListener(it, shimmerPopularUsers, rvPopularUsers)
            }
        }
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
                        searchView.hide()
                        mainViewModel.setSearchUsername(searchView.text.toString())

                        true
                    }
            }
            rvSearchUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                )
                adapter = _searchUsersAdapter.withLoadStateFooter(UsersLoaderStateAdapter {})
            }
            rvPopularUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = _popularUsersAdapter.withLoadStateFooter(PopularUsersLoaderStateAdapter {})
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            mainViewModel.searchUsername.collect {
                with(viewBinding) {
                    searchBar.setText(it)
                }
            }
        }
        lifecycleScope.launch {
            mainViewModel.searchUsers.collect {
                _searchUsersAdapter.submitData(lifecycle, it)
            }
        }
        lifecycleScope.launch {
            mainViewModel.popularUsers.collect {
                _popularUsersAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun loadStateListener(combinedLoadStates: CombinedLoadStates, shimmer: ShimmerFrameLayout, recyclerView: RecyclerView): Unit {
        if (combinedLoadStates.refresh is LoadState.Loading) {
            shimmer.apply {
                startShimmer()
                visibility = View.VISIBLE
            }
            recyclerView.apply {
                visibility = View.GONE
            }
        } else {
            shimmer.apply {
                stopShimmer()
                visibility = View.GONE
            }
            recyclerView.apply {
                visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
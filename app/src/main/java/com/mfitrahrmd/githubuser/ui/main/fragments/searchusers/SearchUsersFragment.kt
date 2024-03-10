package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.adapters.ListUserAdapter
import com.mfitrahrmd.githubuser.adapters.PopularUsersAdapter
import com.mfitrahrmd.githubuser.adapters.SearchUsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchUsersFragment :
    BaseFragment<FragmentSearchUsersBinding, SearchUsersViewModel>(SearchUsersViewModel::class.java) {
    private val _listSearchUsersAdapter: ListUserAdapter = ListUserAdapter(emptyList()).apply {
        setOnItemClickListener {
            findNavController().navigate(
                SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(
                    it.username
                )
            )
        }
    }

    private val _searchUsersAdapter: SearchUsersAdapter = SearchUsersAdapter {
        findNavController().navigate(
            SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(
                it.username
            )
        )
    }

    private val _popularUsersAdapter: PopularUsersAdapter = PopularUsersAdapter {
        findNavController().navigate(
            SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(
                it.username
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
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
                        viewModel.username = searchView.text.toString()
                        lifecycleScope.launch {
                            viewModel.searchUsers()
                        }

                        true
                    }
            }
            rvSearchUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                )
                adapter = _searchUsersAdapter
            }
            rvPopularIndoUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = _popularUsersAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        val position = parent.getChildLayoutPosition(view)
                        when (position) {
                            0 -> {
                                outRect.right =
                                    resources.getDimensionPixelSize(R.dimen.popular_indo_users_spacing)
                            }

                            parent.childCount - 1 -> {
                                outRect.left =
                                    resources.getDimensionPixelSize(R.dimen.popular_indo_users_spacing)
                            }

                            else -> {
                                outRect.right =
                                    resources.getDimensionPixelSize(R.dimen.popular_indo_users_spacing)
                                outRect.left =
                                    resources.getDimensionPixelSize(R.dimen.popular_indo_users_spacing)
                            }
                        }
                    }
                })
            }
        }
    }

    override fun observe() {
        lifecycleScope.launch {
            viewModel.searchUsersPaging.collectLatest { pd ->
                _searchUsersAdapter.submitData(lifecycle, pd)
            }
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.searchUsersState.collect { currentUiState ->
//                    when (currentUiState) {
//                        is BaseState.Success -> {
//                            _listSearchUsersAdapter.setUsers {
//                                currentUiState.data ?: emptyList()
//                            }
//                            with(viewBinding) {
//                                shimmerSearchUsers.apply {
//                                    stopShimmer()
//                                    visibility = View.GONE
//                                }
//                                if (_listSearchUsersAdapter.users.isEmpty()) {
//                                    tvInfo.apply {
//                                        text = getString(R.string.no_user_found, viewModel.username)
//                                        visibility = View.VISIBLE
//                                    }
//                                } else {
//                                    tvInfo.visibility = View.GONE
//                                    rvSearchUsers.visibility = View.VISIBLE
//                                }
//                                tvTitleSearchResult.visibility = View.VISIBLE
//                            }
//                        }
//
//                        is BaseState.Loading -> {
//                            Log.d("LOADING", "loading")
//                            with(viewBinding) {
//                                tvInfo.visibility = View.GONE
//                                shimmerSearchUsers.apply {
//                                    startShimmer()
//                                    visibility = View.VISIBLE
//                                }
//                                rvSearchUsers.visibility = View.GONE
//                            }
//                        }
//
//                        is BaseState.Error -> {
//                            with(viewBinding) {
//                                tvInfo.visibility = View.GONE
//                                shimmerSearchUsers.apply {
//                                    stopShimmer()
//                                    visibility = View.GONE
//                                }
//                                rvSearchUsers.visibility = View.GONE
//                                Toast.makeText(
//                                    view?.context,
//                                    currentUiState.message ?: DEFAULT_ERROR_MESSAGE,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//                        else -> {}
//                    }
//                }
//            }
        }
        lifecycleScope.launch {
            viewModel.popularUsersPaging.collectLatest {
                _popularUsersAdapter.submitData(lifecycle, it)
            }
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.popularIndoUsersState.collect { currentUiState ->
//                    when (currentUiState) {
//                        is BaseState.Success -> {
//                            _popularIndoUsersAdapter.setPopularIndoUsers {
//                                currentUiState.data ?: emptyList()
//                            }
//                            with(viewBinding) {
//                                shimmerPopularIndoUsers.apply {
//                                    stopShimmer()
//                                    visibility = View.GONE
//                                }
//                                rvPopularIndoUsers.visibility = View.VISIBLE
//                            }
//                        }
//
//                        is BaseState.Loading -> {
//                            with(viewBinding) {
//                                shimmerPopularIndoUsers.apply {
//                                    startShimmer()
//                                    visibility = View.VISIBLE
//                                }
//                                rvPopularIndoUsers.visibility = View.GONE
//                            }
//                        }
//
//                        is BaseState.Error -> {
//                            with(viewBinding) {
//                                shimmerPopularIndoUsers.apply {
//                                    stopShimmer()
//                                    visibility = View.GONE
//                                }
//                                rvPopularIndoUsers.visibility = View.GONE
//                                Toast.makeText(
//                                    view?.context,
//                                    if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//                        else -> {}
//                    }
//                }
//            }
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
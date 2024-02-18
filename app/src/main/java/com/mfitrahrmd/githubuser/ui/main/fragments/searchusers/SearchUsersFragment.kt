package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.adapters.PopularIndoUsersAdapter
import com.mfitrahrmd.githubuser.adapters.SearchUsersAdapter
import com.mfitrahrmd.githubuser.base.BaseFragment
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import com.mfitrahrmd.githubuser.base.BaseState
import kotlinx.coroutines.launch

class SearchUsersFragment : BaseFragment<FragmentSearchUsersBinding, SearchUsersViewModel>(SearchUsersViewModel::class.java) {
    private val _searchUsersAdapter: SearchUsersAdapter = SearchUsersAdapter(emptyList()).apply {
        setOnItemClickListener {
            findNavController().navigate(SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(it.login))
        }
    }

    private val _popularIndoUsersAdapter: PopularIndoUsersAdapter =
        PopularIndoUsersAdapter(emptyList()).apply {
            setOnItemClickListener {
                findNavController().navigate(SearchUsersFragmentDirections.actionSearchUsersToDetailUserFragment(it.login))
            }
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
                        lifecycleScope.launch {
                            viewModel.searchUsers(searchView.text.toString())
                        }

                        false
                    }
            }
            rvSearchUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = _searchUsersAdapter
            }
            rvPopularIndoUsers.apply {
                layoutManager = LinearLayoutManager(
                    this@SearchUsersFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = _popularIndoUsersAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        val position = parent.getChildLayoutPosition(view)
                        when(position) {
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchUsersState.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            _searchUsersAdapter.setUsers {
                                currentUiState.data ?: emptyList()
                            }
                            with(viewBinding) {
                                shimmerSearchUsers.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                rvSearchUsers.visibility = View.VISIBLE
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                shimmerSearchUsers.apply {
                                    startShimmer()
                                    visibility = View.VISIBLE
                                }
                                rvSearchUsers.visibility = View.GONE
                            }
                        }

                        is BaseState.Error -> {
                            with(viewBinding) {
                                shimmerSearchUsers.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                rvSearchUsers.visibility = View.GONE
                                Toast.makeText(
                                    view?.context,
                                    if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popularIndoUsersState.collect { currentUiState ->
                    when (currentUiState) {
                        is BaseState.Success -> {
                            _popularIndoUsersAdapter.setPopularIndoUsers {
                                currentUiState.data ?: emptyList()
                            }
                            with(viewBinding) {
                                shimmerPopularIndoUsers.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                rvPopularIndoUsers.visibility = View.VISIBLE
                            }
                        }

                        is BaseState.Loading -> {
                            with(viewBinding) {
                                shimmerPopularIndoUsers.apply {
                                    startShimmer()
                                    visibility = View.VISIBLE
                                }
                                rvPopularIndoUsers.visibility = View.GONE
                            }
                        }

                        is BaseState.Error -> {
                            with(viewBinding) {
                                shimmerPopularIndoUsers.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                rvPopularIndoUsers.visibility = View.GONE
                                Toast.makeText(
                                    view?.context,
                                    if (!currentUiState.message.isNullOrEmpty()) currentUiState.message else DEFAULT_ERROR_MESSAGE,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "unexpected error"
    }
}
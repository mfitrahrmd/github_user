package com.mfitrahrmd.githubuser.ui.main.fragments.searchusers

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.mfitrahrmd.githubuser.R
import com.mfitrahrmd.githubuser.databinding.FragmentSearchUsersBinding
import com.mfitrahrmd.githubuser.ui.AppViewModelProvider
import com.mfitrahrmd.githubuser.ui.UiState
import kotlinx.coroutines.launch

class SearchUsersFragment : Fragment() {
    private lateinit var _binding: FragmentSearchUsersBinding
    private val _viewModel: SearchUsersViewModel by viewModels(factoryProducer = { AppViewModelProvider.Factory })
    private val _searchUsersAdapter: SearchUsersAdapter = SearchUsersAdapter(emptyList())
    private val _popularIndoUsersAdapter: PopularIndoUsersAdapter =
        PopularIndoUsersAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchUsersBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind(_searchUsersAdapter, _popularIndoUsersAdapter)
        observe(_searchUsersAdapter, _popularIndoUsersAdapter)
    }

    private fun bind(
        searchUsersAdapter: SearchUsersAdapter,
        popularIndoUsersAdapter: PopularIndoUsersAdapter
    ) {
        with(_binding) {
            /*
            * Setup search view
            * */
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    lifecycleScope.launch {
                        _viewModel.searchUsers(searchView.text.toString())
                    }
                    false
                }
            rvSearchUsers.layoutManager = LinearLayoutManager(
                this@SearchUsersFragment.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvSearchUsers.adapter = searchUsersAdapter

            rvPopularIndoUsers.layoutManager = LinearLayoutManager(
                this@SearchUsersFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvPopularIndoUsers.adapter = popularIndoUsersAdapter
            rvPopularIndoUsers.addItemDecoration(object : RecyclerView.ItemDecoration() {
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

    private fun observe(
        searchUsersAdapter: SearchUsersAdapter,
        popularIndoUsersAdapter: PopularIndoUsersAdapter
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.searchUsersState.collect { currentUiState ->
                    when (currentUiState) {
                        is UiState.Success -> {
                            searchUsersAdapter.setUsers {
                                currentUiState.data ?: emptyList()
                            }
                            with(_binding) {
                                shimmerSearchUsers.stopShimmer()
                                shimmerSearchUsers.visibility = View.GONE
                                rvSearchUsers.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Loading -> {
                            with(_binding) {
                                shimmerSearchUsers.startShimmer()
                                shimmerSearchUsers.visibility = View.VISIBLE
                                rvSearchUsers.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            with(_binding) {
                                shimmerSearchUsers.stopShimmer()
                                shimmerSearchUsers.visibility = View.GONE
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
                _viewModel.popularIndoUsersState.collect { currentUiState ->
                    when (currentUiState) {
                        is UiState.Success -> {
                            popularIndoUsersAdapter.setPopularIndoUsers {
                                currentUiState.data ?: emptyList()
                            }
                            with(_binding) {
                                shimmerPopularIndoUsers.stopShimmer()
                                shimmerPopularIndoUsers.visibility = View.GONE
                                rvPopularIndoUsers.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Loading -> {
                            with(_binding) {
                                shimmerPopularIndoUsers.startShimmer()
                                shimmerPopularIndoUsers.visibility = View.VISIBLE
                                rvPopularIndoUsers.visibility = View.GONE
                            }
                        }

                        is UiState.Error -> {
                            with(_binding) {
                                shimmerPopularIndoUsers.stopShimmer()
                                shimmerPopularIndoUsers.visibility = View.GONE
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
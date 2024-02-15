package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.githubuser.adapters.ListUserAdapter
import com.mfitrahrmd.githubuser.databinding.FragmentUserFollowBinding
import com.mfitrahrmd.githubuser.models.User
import com.mfitrahrmd.githubuser.ui.UiState
import com.mfitrahrmd.githubuser.ui.main.fragments.searchusers.SearchUsersFragment
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserFollowFragment(private val _dataFlow: StateFlow<UiState<List<User>>>?) : Fragment() {
    private lateinit var _binding: FragmentUserFollowBinding
    private val _listUserFollowAdapter = ListUserAdapter(emptyList())

    constructor() : this(null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
        observe()
    }

    private fun bind() {
        with(_binding) {
            rvFollow.layoutManager = LinearLayoutManager(this@UserFollowFragment.context, LinearLayoutManager.VERTICAL, false)
            rvFollow.adapter = _listUserFollowAdapter
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _dataFlow?.collect { currentUiState ->
                    when(currentUiState) {
                        is UiState.Success -> {
                            with(_binding) {
                                shimmerFollow.stopShimmer()
                                shimmerFollow.visibility = View.GONE
                            }

                            _listUserFollowAdapter.setUsers {
                                currentUiState.data ?: it
                            }
                        }
                        is UiState.Loading -> {
                            with(_binding) {
                                shimmerFollow.startShimmer()
                                shimmerFollow.visibility = View.VISIBLE
                            }
                        }
                        is UiState.Error -> {
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
}
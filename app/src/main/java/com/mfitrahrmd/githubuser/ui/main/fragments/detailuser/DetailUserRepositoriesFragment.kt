package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mfitrahrmd.githubuser.ui.AppViewModelProvider
import com.mfitrahrmd.githubuser.ui.GithubUserTheme
import kotlinx.coroutines.launch

class DetailUserRepositoriesFragment : Fragment() {
    private val _viewModel: DetailUserRepositoriesViewModel by viewModels(factoryProducer = {AppViewModelProvider.Factory})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = UserFollowingFragmentArgs.fromBundle(arguments as Bundle).username
        _viewModel.setUsername(username)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vm: DetailUserRepositoriesViewModel by viewModels(factoryProducer = {AppViewModelProvider.Factory})
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GithubUserTheme {
                    Screen(viewModel = _viewModel)
                }
            }
        }
    }

    @Composable
    fun Screen(
        viewModel: DetailUserRepositoriesViewModel
    ) {
        val uiState by viewModel.uiState.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                viewModel.getRepositoriesCount()
            }
        }
        Column {
            when (uiState.status) {
                is Status.Loading -> {
                    Text(text = "Loading...", textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
                }
                is Status.Success -> {
                    Text(text = uiState.repositoriesCount.toString(), textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
                }
                else -> {}
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(detailUserRepositoriesFragmentArgs: DetailUserRepositoriesFragmentArgs) =
            DetailUserRepositoriesFragment().apply {
                arguments = detailUserRepositoriesFragmentArgs.toBundle()
            }
    }
}
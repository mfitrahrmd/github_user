package com.mfitrahrmd.githubuser.ui.main.fragments.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.mfitrahrmd.githubuser.ui.GithubUserTheme

class DetailUserRepositoriesFragment : Fragment() {
    private lateinit var _username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _username = UserFollowingFragmentArgs.fromBundle(arguments as Bundle).username
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                GithubUserTheme {
                    Column {
                        Text(text = _username)
                    }
                }
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
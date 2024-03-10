package com.mfitrahrmd.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mfitrahrmd.githubuser.databinding.ItemLoadStateBinding

class LoaderStateAdapter(private val _retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {
    class LoaderViewHolder(private val _binding: ItemLoadStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(_binding.root) {
        init {
            _binding.btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading) {
                _binding.shimmer.apply {
                    startShimmer()
                    visibility = View.VISIBLE
                }
            } else {
                _binding.shimmer.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
            }
            _binding.btnRetry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder(
            ItemLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), _retry
        )
    }
}
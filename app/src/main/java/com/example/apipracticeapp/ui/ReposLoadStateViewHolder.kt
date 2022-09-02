package com.example.apipracticeapp.ui

import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadStateAdapter
import com.example.apipracticeapp.databinding.ReposLoadStateFooterViewItemBinding


class RepoLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<RepoLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = ReposLoadStateFooterViewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState, retry)
    }

    class ViewHolder(
        private val binding: ReposLoadStateFooterViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState, retry: () -> Unit) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error

            binding.retryButton.setOnClickListener {
                retry()
            }
            binding.executePendingBindings()
        }
    }
}
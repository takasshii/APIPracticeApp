package com.example.apipracticeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apipracticeapp.data.Item
import com.example.apipracticeapp.databinding.FragmentRankingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels()

    private val customAdapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
        override fun itemClick(item: Item) {
            viewModel.nextPage(item)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // bindingの結びつけ
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        // リストの管理
        // リストに区切り線を入れる
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        // タップ処理を定義
        val adapter = customAdapter.withLoadStateHeaderAndFooter(
            header = RepoLoadStateAdapter(customAdapter::retry),
            footer = RepoLoadStateAdapter(customAdapter::retry)
        )
        // recyclerViewにアダプターを結びつけ
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        fun onRefresh() {
            binding.recyclerView.scrollToPosition(0)
            viewModel.getTime()
            customAdapter.refresh()
        }

        fun retry() {
            customAdapter.retry()
        }

        // タップした時の処理（APIを呼び出す）
        binding.reloadButton.setOnClickListener {
            onRefresh()
        }

        binding.retryEmptyButton.setOnClickListener {
            customAdapter.refresh()
        }

        // Flowを監視
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.repositories.collectLatest { pagingData ->
                customAdapter.submitData(pagingData)
            }
        }

        // Flowを監視
        viewLifecycleOwner.lifecycleScope.launch {
            customAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && customAdapter.itemCount == 0

                // リストが空の時に
                binding.recyclerView.isVisible = !isListEmpty
                binding.emptyText.isVisible = isListEmpty
                // 初回のローディングの表示
                binding.view.alpha = if (loadState.source.refresh is LoadState.Loading) {
                    0.5F
                } else {
                    0F
                }
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // 初回のエラーの表示
                binding.retryEmptyButton.isVisible = loadState.source.refresh is LoadState.Error

            }
        }

        // LiveDataを監視
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.time != null) {
                // 時間をセット
                binding.timeText.text = uiState.time
            }
            if (uiState.events.firstOrNull() != null) {
                when (val event = uiState.events.firstOrNull()) {
                    is Event.NextPage -> {
                        navigationResultFragment(event.item)
                        // イベントを消費
                        viewModel.consumeEvent(event)
                    }
                    else -> {
                        Log.d("TAG", "handleWeatherUpdate: else pattern")
                    }
                }
            }
        }
        return binding.root
    }

    //bindingの解放
    override fun onDestroyView() {
        super.onDestroyView()
        //bindingの解放
        _binding = null
    }

    private fun navigationResultFragment(item: Item) {
        val action = RankingFragmentDirections
            .actionRankingFragmentToResultFragment(item)
        findNavController().navigate(action)
    }
}
package com.example.apipracticeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apipracticeapp.R
import com.example.apipracticeapp.data.Content
import com.example.apipracticeapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // bindingの結びつけ
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        // リストの管理
        // リストに区切り線を入れる
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        // タップ処理を定義
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: Content) {
                viewModel.nextPage()
            }
        })
        // recyclerViewにアダプターを結びつけ
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        // タップした時の処理（APIを呼び出す）
        binding.reloadButton.setOnClickListener {
            viewModel.fetchAPI()
        }

        // LiveDataを監視
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            if (uiState.events.firstOrNull() != null) {
                when (val event = uiState.events.firstOrNull()) {
                    is Event.Success -> {
                        // リストに値をセット
                        adapter.submitList(uiState.repositories?.items)
                        // イベントを消費
                        viewModel.consumeEvent(event)
                    }
                    is Event.Error -> {
                        // ここでダイアログの表示を行う（未実装）
                        // イベントを消費
                        viewModel.consumeEvent(event)
                    }
                    is Event.NextPage -> {
                        navigationResultFragment()
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

    private fun navigationResultFragment() {
        findNavController().navigate(R.id.resultFragment)
    }
}
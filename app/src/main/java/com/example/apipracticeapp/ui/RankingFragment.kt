package com.example.apipracticeapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apipracticeapp.data.Item
import com.example.apipracticeapp.databinding.FragmentRankingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels()

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
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: Item) {
                viewModel.nextPage(item)
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
            if(uiState.repositories != null) {
                // リストに値をセット
                adapter.submitList(uiState.repositories)
            }
            if(uiState.time != null) {
                // 時間をセット
                binding.timeText.text = uiState.time
            }
            if (uiState.events.firstOrNull() != null) {
                when (val event = uiState.events.firstOrNull()) {
                    is Event.Success -> {
                        // イベントを消費
                        viewModel.consumeEvent(event)
                    }
                    is Event.Error -> {
                        // ここでダイアログの表示を行う
                        showNoticeDialog()
                        // イベントを消費
                        viewModel.consumeEvent(event)
                    }
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

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                // EditTextのワードを含むItemを返す
                val filteredList = viewModel.filteringRankingList(editText.text.toString())
                adapter.submitList(filteredList)
                // 検索後にキーボードを隠す
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        return binding.root
    }

    //bindingの解放
    override fun onDestroyView() {
        super.onDestroyView()
        //bindingの解放
        _binding = null
    }

    private fun showNoticeDialog() {
        val dialog = ErrorDialogFragment(object : ErrorDialogFragment.NoticeDialogListener {
            override fun positiveClick() {
                viewModel.fetchAPI()
            }
        })
        dialog.show(childFragmentManager, "APIError")
    }

    private fun navigationResultFragment(item: Item) {
        val action = RankingFragmentDirections
            .actionRankingFragmentToResultFragment(item)
        findNavController().navigate(action)
    }
}
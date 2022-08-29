package com.example.apipracticeapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.example.apipracticeapp.data.APIResult
import com.example.apipracticeapp.data.GithubAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val githubAPIRepository: GithubAPIRepository
) : ViewModel() {
    // 状態変数を管理するLiveData
    private val _uiState =
        MutableLiveData(UiState(repositories = null, proceeding = false))
    val uiState: LiveData<UiState>
        get() = _uiState

    // APIを取得する関数
    fun fetchAPI() {
        // ローディング開始
        _uiState.value = _uiState.value?.copy(proceeding = true)

        // API取得(APIResultで結果をラップ)
        viewModelScope.launch {
            // ランキングを取得する
            // headerでjsonを指定, inputTextでランキングを指定
            val result = githubAPIRepository.getRepository(
                header = "application/vnd.github.v3+json", inputText = "stars"
            )

            // レスポンスに応じてLiveDataに値を格納
            _uiState.value = when (result) {
                is APIResult.Success -> {
                    // ViewModelイベント発行
                    val newEvents = _uiState.value?.events?.plus(Event.Success)
                    //　値をセット
                    _uiState.value?.copy(
                        events = newEvents ?: emptyList(),
                        repositories = result.data
                    )
                }
                // エラーが生じていた場合 -> エラーダイアログを表示
                is APIResult.Error -> {
                    // ViewModelイベント発行
                    val newEvents =
                        _uiState.value?.events?.plus(Event.Error(result.exception.toString()))
                    // 値をセット
                    _uiState.value?.copy(events = newEvents ?: emptyList())
                }
            }

            // ローディングを終了
            _uiState.value = _uiState.value?.copy(proceeding = false)
        }
    }

    // イベントを消費する関数
    fun consumeEvent(event: Event) {
        val newEvents = _uiState.value?.events?.filterNot { it == event }
        _uiState.value = uiState.value?.copy(events = newEvents ?: emptyList())
    }

    // 次のページに進むEventを発行する関数
    fun nextPage() {
        val newEvents = _uiState.value?.events?.plus(Event.NextPage)
        _uiState.value = _uiState.value?.copy(events = newEvents ?: emptyList())
    }
}
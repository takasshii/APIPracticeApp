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
    private val _uiState =
        MutableLiveData(UiState(repositories = null, proceeding = false))
    val uiState: LiveData<UiState>
        get() = _uiState

    // APIを取得する関数
    fun fetchAPI(inputText: String) {
        _uiState.value = _uiState.value?.copy(proceeding = true)

        // API取得(APIResultで結果をラップ)
        viewModelScope.launch {
            val result = githubAPIRepository.getRepository(
                header = "application/vnd.github.v3+json", inputText = "stars"
            )
            Log.v("result", result.toString())
            _uiState.value = when (result) {
                is APIResult.Success -> {
                    // liveDataに値をセット
                    val newEvents = _uiState.value?.events?.plus(Event.Success)
                    _uiState.value?.copy(
                        events = newEvents ?: emptyList(),
                        repositories = result.data
                    )
                }
                // エラーが生じていた場合 -> エラー画像を表示
                is APIResult.Error -> {
                    val newEvents =
                        _uiState.value?.events?.plus(Event.Error(result.exception.toString()))
                    _uiState.value?.copy(events = newEvents ?: emptyList())
                }
            }
            _uiState.value = _uiState.value?.copy(proceeding = false)
        }
    }

    fun consumeEvent(event: Event) {
        val newEvents = _uiState.value?.events?.filterNot { it == event }
        _uiState.value = uiState.value?.copy(events = newEvents ?: emptyList())
    }

    fun nextPage() {
        val newEvents = _uiState.value?.events?.plus(Event.NextPage)
        _uiState.value = _uiState.value?.copy(events = newEvents ?: emptyList())
    }
}
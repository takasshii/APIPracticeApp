package com.example.apipracticeapp.ui

import androidx.lifecycle.*
import androidx.paging.*
import com.example.apipracticeapp.data.*
import com.example.apipracticeapp.data.GithubAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    githubAPIRepository: GithubAPIRepository
) : ViewModel() {
    // 状態変数を管理するLiveData
    private val _uiState =
        MutableLiveData(UiState(time = null))
    val uiState: LiveData<UiState>
        get() = _uiState

    // UiStateとは別でFlowを用意する必要があるみたいなので分けました
    val repositories: Flow<PagingData<Item>> =
        githubAPIRepository.getRepository().cachedIn(viewModelScope)

    // 時間を取得する関数
    fun getTime() {
        // 現在時刻の取得
        val dateFormat = SimpleDateFormat("MM月dd日 HH:mm:ss現在")
        val nowTime = Date(System.currentTimeMillis())
        val formatNowTime = dateFormat.format(nowTime)

        //　値をセット
        _uiState.value = _uiState.value?.copy(
            time = formatNowTime
        )
    }

    // イベントを消費する関数
    fun consumeEvent(event: Event) {
        val newEvents = _uiState.value?.events?.filterNot { it == event }
        _uiState.value = uiState.value?.copy(events = newEvents ?: emptyList())
    }

    // 次のページに進むEventを発行する関数
    fun nextPage(item: Item) {
        val newEvents = _uiState.value?.events?.plus(Event.NextPage(item))
        _uiState.value = _uiState.value?.copy(events = newEvents ?: emptyList())
    }
}
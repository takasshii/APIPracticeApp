package com.example.apipracticeapp.ui

import com.example.apipracticeapp.data.Item

data class UiState(
    val events: List<Event> = emptyList(),
    val time: String?,
)

sealed interface Event {
    object Success : Event
    data class Error(val message: String) : Event
    data class NextPage(val item: Item) : Event
}
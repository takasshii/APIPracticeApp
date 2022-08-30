package com.example.apipracticeapp.ui

import com.example.apipracticeapp.data.Item
import com.example.apipracticeapp.data.JsonGithub
import java.util.*

data class UiState(
    val repositories: List<Item>?,
    val events: List<Event> = emptyList(),
    val time: String?,
    val proceeding: Boolean
)

sealed interface Event {
    object Success : Event
    data class Error(val message: String) : Event
    data class NextPage(val item: Item): Event
}
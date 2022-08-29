package com.example.apipracticeapp.ui

import com.example.apipracticeapp.data.Item
import com.example.apipracticeapp.data.JsonGithub
import java.util.*

data class UiState(
    val repositories: JsonGithub?,
    val events: List<Event> = emptyList(),
    val proceeding: Boolean
)

sealed interface Event {
    data class Success(val time: String) : Event
    data class Error(val message: String) : Event
    data class NextPage(val item: Item): Event
}
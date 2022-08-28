package com.example.apipracticeapp.ui

import com.example.apipracticeapp.data.JsonGithub

data class UiState(
    val repositories: JsonGithub?,
    val events: List<Event> = emptyList(),
    val proceeding: Boolean
)

sealed interface Event {
    object Success : Event
    data class Error(val message: String) : Event
    object NextPage: Event
}
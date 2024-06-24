package com.sa.mytodo.presentation.events

sealed class TodoUiEvent {
    data class ShowError(val message: String?): TodoUiEvent()
    data class Success(val message: String): TodoUiEvent()
    data object Loading: TodoUiEvent()
}
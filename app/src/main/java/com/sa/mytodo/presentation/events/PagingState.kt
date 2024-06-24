package com.sa.mytodo.presentation.events


sealed class PagingState{
    data object Idle: PagingState()
    data object Loading: PagingState()
    data object Paginating: PagingState()
    data class Error(val errorMsg:String?): PagingState()
    data class PaginatingError(val errorMsg:String?): PagingState()
}
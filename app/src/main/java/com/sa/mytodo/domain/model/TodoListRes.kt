package com.sa.mytodo.domain.model


import com.google.gson.annotations.SerializedName

data class TodoListRes(
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("skip")
    val skip: Int?,
    @SerializedName("todos")
    val todos: List<Todo>?,
    @SerializedName("total")
    val total: Int?
)


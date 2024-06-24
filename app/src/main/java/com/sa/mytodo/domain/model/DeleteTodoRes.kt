package com.sa.mytodo.domain.model


import com.google.gson.annotations.SerializedName

data class DeleteTodoRes(
    @SerializedName("completed")
    val completed: Boolean?,
    @SerializedName("deletedOn")
    val deletedOn: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("isDeleted")
    val isDeleted: Boolean?,
    @SerializedName("todo")
    val todo: String?,
    @SerializedName("userId")
    val userId: Int?
)
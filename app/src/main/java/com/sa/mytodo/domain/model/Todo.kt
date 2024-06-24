package com.sa.mytodo.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Todo(
    @SerializedName("completed")
    val completed: Boolean?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("todo")
    val todoDesc: String?,
    @SerializedName("userId")
    val userId: Int?,
    val addFromLocal:Boolean=false
){
    @PrimaryKey(autoGenerate = true)
    var uid: Int=0
}
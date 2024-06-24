package com.sa.mytodo.data.data_sources.remote

sealed class Resource<T>(
    val data:T?=null,
    val error:String?=null
){
    class Loading<T>: Resource<T>()
    class Error<T>(error: String?): Resource<T>(error = error?:"")
    class Success<T>(data: T?): Resource<T>(data = data)
}
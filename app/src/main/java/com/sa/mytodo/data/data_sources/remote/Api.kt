package com.sa.mytodo.data.data_sources.remote

import com.sa.mytodo.domain.model.DeleteTodoRes
import com.sa.mytodo.domain.model.Todo
import com.sa.mytodo.domain.model.TodoListRes
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Api  {
    @GET("todos")
    suspend fun getTodos(@Query("limit") limit: Int,@Query("skip")skip:Int): Result<TodoListRes>

    @POST("todos/add")
    suspend fun addTodo(@Body todoBody: Todo):Result<Todo>

    @PUT("todos/{id}")
    suspend fun updateTodo(@Path("id") id:Int, @Body todoBody: HashMap<String,Any>):Result<Todo>

    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id:Int):Result<DeleteTodoRes>
}
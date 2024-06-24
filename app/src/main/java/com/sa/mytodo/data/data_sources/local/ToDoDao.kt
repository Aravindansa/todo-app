package com.sa.mytodo.data.data_sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.sa.mytodo.domain.model.Todo
import kotlinx.coroutines.flow.Flow


@Dao
interface ToDoDao {
    @Upsert
    suspend fun upsertTodos(todos: List<Todo>)

    @Query("DELETE FROM todo")
    suspend fun clearAllTodos()

    @Insert
    suspend fun addToDo(todo: Todo)

    @Query("UPDATE todo SET  completed = :completed WHERE uid = :id")
    suspend fun updateCompleted(id: Int, completed: Boolean)

    @Query("DELETE FROM todo WHERE uid = :uid")
    suspend fun deleteById(uid:Int);

    @Query("SELECT * FROM todo WHERE uid = :uid")
    suspend fun getTodoByUid(uid:Int):Todo?

    @Query("UPDATE todo SET  todoDesc = :todoDesc WHERE uid = :uid")
    suspend fun updateTodoDescByUid(uid: Int, todoDesc: String)


    @Query("""
        SELECT * FROM todo
        ORDER BY 
            CASE 
                WHEN addFromLocal = 1 THEN uid 
                ELSE null 
            END DESC,
            CASE 
                WHEN addFromLocal = 0 THEN uid 
                ELSE null 
            END ASC
    """)
    fun getAllTodoListFlow(): Flow<List<Todo>>

}
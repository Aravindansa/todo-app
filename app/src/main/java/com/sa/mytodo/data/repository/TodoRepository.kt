package com.sa.mytodo.data.repository

import android.app.Application
import androidx.room.withTransaction
import com.sa.mytodo.data.data_sources.local.AppDatabase
import com.sa.mytodo.data.data_sources.local.LocalSession
import com.sa.mytodo.domain.model.Todo
import com.sa.mytodo.data.data_sources.remote.Api
import com.sa.mytodo.data.data_sources.remote.Resource
import com.sa.mytodo.domain.model.DeleteTodoRes
import com.sa.mytodo.domain.model.TodoListRes
import com.sa.mytodo.domain.util.Constants
import com.sa.mytodo.domain.util.MyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class TodoRepository(
    private val app: Application,
    private val api: Api,
    private val appDatabase: AppDatabase
) {
    fun getTodoListFromServer(page:Int):Flow<Resource<TodoListRes>>{
        return flow<Resource<TodoListRes>> {
            api.getTodos(limit = Constants.LIMIT_PAGING, skip = page).onSuccess {
                if (!it.todos.isNullOrEmpty()){
                    LocalSession.setCurrentPageSkip(app,page)
                }
                emit(Resource.Success(it))
            }.onFailure {
                emit(Resource.Error(it.message))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getLocalTodos():Flow<List<Todo>>{
        return appDatabase.appDao().getAllTodoListFlow()
    }


    suspend fun insertTodoListToLocal(pageSkip:Int,todoList:List<Todo>?){
        appDatabase.withTransaction {
            if (pageSkip==0){
                appDatabase.appDao().clearAllTodos()
            }
            todoList?.let {list->
                appDatabase.appDao().upsertTodos(list)
            }
        }
    }

    fun getNextPageSkip():Int{
        val page= LocalSession.getCurrentPageSkip(app)
        return if (page==-1){
            0
        }else{
            page+20
        }
    }

    fun completedTodo(todo: Todo,isCompleted:Boolean):Flow<Resource<Boolean>> {
        return flow<Resource<Boolean>> {
            if (todo.addFromLocal || !MyUtil.isOnline(app)){
                appDatabase.appDao().updateCompleted(todo.uid,isCompleted)
                emit(Resource.Success(true))
            }else{
                emit(Resource.Loading())
                val map=HashMap<String,Any>()
                map["completed"]=isCompleted
                api.updateTodo(todo.id,map).onSuccess {
                    appDatabase.appDao().updateCompleted(todo.uid,it.completed?:false)
                    emit(Resource.Success(true))
                }.onFailure {
                    emit(Resource.Error(it.message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun deleteTodo(todo: Todo):Flow<Resource<DeleteTodoRes?>> {
        return flow<Resource<DeleteTodoRes?>> {
            if (todo.addFromLocal || !MyUtil.isOnline(app)){
                appDatabase.appDao().deleteById(todo.uid)
                emit(Resource.Success(null))
            }else{
                emit(Resource.Loading())
                api.deleteTodo(todo.id).onSuccess {
                    appDatabase.appDao().deleteById(todo.uid)
                    emit(Resource.Success(it))
                }.onFailure {
                    emit(Resource.Error(it.message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun addTodo(todo: Todo):Flow<Resource<Todo>>{
        return flow <Resource<Todo>>{
            emit(Resource.Loading())
            api.addTodo(todo).onSuccess {
                appDatabase.appDao().addToDo(it.copy(addFromLocal = true))
                emit(Resource.Success(it))
            }.onFailure {
                emit(Resource.Error(it.message))
            }
        }.flowOn(Dispatchers.IO)

    }

    suspend fun getTodoById(uid: Int):Todo?{
        return withContext(Dispatchers.IO){
            appDatabase.appDao().getTodoByUid(uid)
        }
    }

    fun updateTodo(todo: Todo,todoDesc:String):Flow<Resource<Boolean>> {
        return flow<Resource<Boolean>> {
            if (todo.addFromLocal || !MyUtil.isOnline(app)){
                appDatabase.appDao().updateTodoDescByUid(todo.uid,todoDesc)
                emit(Resource.Success(true))
            }else{
                emit(Resource.Loading())
                val map=HashMap<String,Any>()
                map["todo"]=todoDesc
                api.updateTodo(todo.id,map).onSuccess {
                    appDatabase.appDao().updateTodoDescByUid(todo.uid,it.todoDesc?:"")
                    emit(Resource.Success(true))
                }.onFailure {
                    emit(Resource.Error(it.message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getString(id: Int):String{
        return app.getString(id)
    }
}




package com.sa.mytodo.presentation.add_todo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.mytodo.R
import com.sa.mytodo.data.data_sources.remote.Resource
import com.sa.mytodo.data.repository.TodoRepository
import com.sa.mytodo.presentation.events.TodoUiEvent
import com.sa.mytodo.domain.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TodoRepository
):ViewModel() {
    private val _todoDesc = MutableStateFlow("")
    val todoDesc = _todoDesc.asStateFlow()
    private val _eventFlow = MutableSharedFlow<TodoUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private var currentTodo:Todo?=null
    private val _isUpdate = MutableStateFlow(false)
    val isUpdate = _isUpdate.asStateFlow()
    init {
        savedStateHandle.get<Int>("uid")?.let {uid->
            if (uid!=-1){
                viewModelScope.launch {
                    repository.getTodoById(uid)?.let {
                        currentTodo=it
                        setTodoDesc(it.todoDesc?:"")
                        _isUpdate.value=true
                    }
                }
            }
        }
    }
    fun setTodoDesc(todo: String) {
        _todoDesc.value = todo
    }
    fun submitTodo(){
        viewModelScope.launch {
            if(isUpdate.value){
                updateTodo()
            }else{
                addNewTodo()
            }
        }
    }
    private suspend fun updateTodo(){
        currentTodo?.let {
            repository.updateTodo(it,todoDesc.value).collect{res->
                when(res){
                    is Resource.Loading->{
                        _eventFlow.emit(TodoUiEvent.Loading)
                    }

                    is Resource.Error->{
                        _eventFlow.emit(TodoUiEvent.ShowError(res.error))
                    }

                    is Resource.Success->{
                        _eventFlow.emit(TodoUiEvent.Success(getStrMsg(R.string.todo_updated)))
                    }
                }
            }
        }
    }
    private suspend fun addNewTodo(){
        repository.addTodo(Todo(
            todoDesc = todoDesc.value,
            userId = 5,
            completed = false,
            id = 5
        )).collect{res->
            when(res){
                is Resource.Loading->{
                    _eventFlow.emit(TodoUiEvent.Loading)
                }
                is Resource.Error->{
                    _eventFlow.emit(TodoUiEvent.ShowError(res.error))
                }
                is Resource.Success->{
                    _eventFlow.emit(TodoUiEvent.Success(getStrMsg(R.string.todo_updated)))
                }
            }
        }
    }
    private fun getStrMsg(id:Int):String{
        return repository.getString(id)
    }

}
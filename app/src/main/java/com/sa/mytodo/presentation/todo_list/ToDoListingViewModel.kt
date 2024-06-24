package com.sa.mytodo.presentation.todo_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sa.mytodo.R
import com.sa.mytodo.data.data_sources.remote.Resource
import com.sa.mytodo.data.repository.TodoRepository
import com.sa.mytodo.presentation.events.PagingState
import com.sa.mytodo.domain.model.Todo
import com.sa.mytodo.presentation.events.TodoUiEvent
import com.sa.mytodo.domain.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListingViewModel @Inject constructor(
    private val repository: TodoRepository,
):ViewModel() {
    private var pageSkip by mutableIntStateOf(repository.getNextPageSkip())
    var canPaginate by mutableStateOf(false)
    var pagingState by mutableStateOf<PagingState>(PagingState.Idle)
    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList: StateFlow<List<Todo>> get() = _todoList.asStateFlow()
    private val _todoEventFlow = MutableSharedFlow<TodoUiEvent>()
    val todEventFlow = _todoEventFlow.asSharedFlow()
    private var currentDeleteTodo:Todo?=null

    init {
        viewModelScope.launch {
            repository.getLocalTodos().collect { itemList ->
                _todoList.value = itemList
            }
        }
        if (pageSkip==0)
            getTodos()
        else
            canPaginate=true
    }

     fun getTodos(){
        viewModelScope.launch {
            if (pageSkip == 0 || (pageSkip != 0 && canPaginate) && pagingState == PagingState.Idle){
                pagingState = if (pageSkip == 0) PagingState.Loading else PagingState.Paginating
                repository.getTodoListFromServer(pageSkip).collect{
                    if (it is Resource.Success){
                        canPaginate = it.data?.todos?.size == Constants.SKIP_PAGING
                        repository.insertTodoListToLocal(pageSkip,it.data?.todos)
                        pagingState = PagingState.Idle
                        if (canPaginate){
                            pageSkip+= Constants.SKIP_PAGING
                        }
                    }else if (it is Resource.Error){
                        pagingState = if (pageSkip == 0) {
                            PagingState.Error(it.error)
                        } else{
                            PagingState.PaginatingError(it.error)
                        }
                    }
                }
            }
        }
    }

    fun onCompletedCheckChange(item: Todo, checked: Boolean) {
        viewModelScope.launch {
            repository.completedTodo(item, checked).collect{
                when(it){
                    is Resource.Loading->{
                        _todoEventFlow.emit(TodoUiEvent.Loading)
                    }
                    is Resource.Error->{
                        _todoEventFlow.emit(TodoUiEvent.ShowError(it.error))
                    }
                    is Resource.Success->{
                        _todoEventFlow.emit(TodoUiEvent.Success(""))
                    }
                }
            }
        }
    }

    fun setCurrentDeleteTodo(todo: Todo?){
        currentDeleteTodo=todo
    }

    fun deleteTodo(){
        currentDeleteTodo?.let {todo->
            viewModelScope.launch{
                repository.deleteTodo(todo).collect{
                    when(it){
                        is Resource.Loading->{
                            _todoEventFlow.emit(TodoUiEvent.Loading)
                        }
                        is Resource.Error->{
                            _todoEventFlow.emit(TodoUiEvent.ShowError(it.error))
                        }
                        is Resource.Success->{
                            _todoEventFlow.emit(TodoUiEvent.Success(getStrMsg(R.string.todo_successfully_deleted)))
                        }
                    }
                }
            }
        }
    }

    private fun getStrMsg(id:Int):String=repository.getString(id)

}
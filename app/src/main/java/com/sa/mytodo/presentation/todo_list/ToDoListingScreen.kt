package com.sa.mytodo.presentation.todo_list


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sa.mytodo.R
import com.sa.mytodo.presentation.events.PagingState
import com.sa.mytodo.domain.model.Todo
import com.sa.mytodo.presentation.events.TodoUiEvent
import com.sa.mytodo.presentation.components.CustomAlertDialog
import com.sa.mytodo.presentation.components.DialogLoader
import com.sa.mytodo.ui.navigation.Screen
import com.sa.mytodo.ui.theme.MyTodoTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ToDoListingScreen(
    navController: NavController,
    viewModel: ToDoListingViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val lazyColumnListState = rememberLazyListState()
    val showUpButton by remember {
        derivedStateOf {
            lazyColumnListState.firstVisibleItemIndex>0
        }
    }
    val shouldStartPaginate by remember {
        derivedStateOf {
            viewModel.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }
    val todos by viewModel.todoList.collectAsStateWithLifecycle()

    var loader by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(Pair(false,"")) }
    var confirmDeleteAlert by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = shouldStartPaginate) {
        if (shouldStartPaginate && viewModel.pagingState == PagingState.Idle)
            viewModel.getTodos()
    }
    LaunchedEffect(key1 = true) {
        viewModel.todEventFlow.collectLatest { event ->
            when(event) {
                is TodoUiEvent.Loading->{
                    loader=true
                }
                is TodoUiEvent.ShowError->{
                    loader=false
                    showError= Pair(true,event.message?:"")
                }
                is TodoUiEvent.Success->{
                    loader=false
                }
            }
        }
    }
    TodoList(
        lazyColumnListState = lazyColumnListState,
        pagingState =viewModel.pagingState,
        todos=todos,
        showUpButton = showUpButton,
        onScrollUp = {
            scope.launch {
                lazyColumnListState.animateScrollToItem(0)
            }
        },
        onRefresh = {
            viewModel.pagingState = PagingState.Idle
            viewModel.getTodos() }
        ,
        onAddTodo = {
            navController.navigate("${Screen.AddToDo.route}/-1")
        },
        onDeletedTodo = {
            it?.let { todo ->
                viewModel.setCurrentDeleteTodo(todo)
                confirmDeleteAlert=true
            }
        },
        onViewTodo = {
            it?.let {
                navController.navigate("${Screen.AddToDo.route}/${it.uid}")
            }

        },
        onCompletedCheckChange = {check,todo->
            todo?.let { viewModel.onCompletedCheckChange(todo,check) }
        }

    )
    if (confirmDeleteAlert){
        CustomAlertDialog(
            dialogText = stringResource(R.string.are_you_sure_you_want_delete_todo),
            confirmBtnText = stringResource(id = R.string.delete),
            onDismissRequest = {confirmDeleteAlert= false },
            onConfirmation = {
                confirmDeleteAlert= false
                viewModel.deleteTodo()
            }
        )
    }
    if (loader){
        DialogLoader()
    }
    if (showError.first){
        CustomAlertDialog(
            dialogText = showError.second,
            onDismissRequest = {showError= Pair(false,"") },
            onConfirmation = {showError= Pair(false,"") }
        )
    }
}

@Composable
fun TodoList(
    modifier: Modifier=Modifier,
    lazyColumnListState: LazyListState,
    pagingState: PagingState,
    todos:List<Todo>,
    showUpButton:Boolean,
    onScrollUp:()->Unit,
    onRefresh:()->Unit,
    onAddTodo: () -> Unit,
    onViewTodo:(Todo?)->Unit,
    onDeletedTodo:(Todo?)->Unit,
    onCompletedCheckChange:(Boolean,Todo?)->Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick ={onAddTodo()},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_todo))
            }
        }
    ){paddingValues ->
       Box(modifier = Modifier
           .padding(paddingValues)
           .fillMaxSize() ){
           Column (modifier= Modifier
               .fillMaxSize()){
               Text(
                   text = "My Todos",
                   style = MaterialTheme.typography.titleLarge,
                   color = MaterialTheme.colorScheme.onPrimary,
                   modifier = Modifier.padding(
                       start = 20.dp,
                       end = 20.dp,
                       top = 15.dp,
                       bottom = 10.dp
                   )
               )
               LazyColumn(
                   state = lazyColumnListState,
                   modifier = Modifier
                       .fillMaxSize(),
                   contentPadding = PaddingValues(bottom = 65.dp)
               ){
                   items(todos,key = {it.uid}){
                       ToDoListItem(
                           modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                           todo = it,
                           onDelete = onDeletedTodo,
                           onView = onViewTodo,
                           onCompletedCheckChange = onCompletedCheckChange
                       )
                   }
                   item {
                       when (pagingState) {
                           is PagingState.PaginatingError->{
                               val message= pagingState.errorMsg
                               PaginateError(errorMsg = message, onRetry = onRefresh)
                           }
                           is PagingState.Paginating -> {
                               PaginateLoading()
                           }
                           else->{}
                       }
                   }
               }
           }
           if (pagingState is PagingState.Loading){
               Loader(modifier = Modifier.align(Alignment.Center))
           }else if (pagingState is PagingState.Error){
               val message= pagingState.errorMsg
               ShowError(
                   modifier= Modifier.align(Alignment.Center),
                   errorMsg = message,
                   onRetry = onRefresh
               )
           }
           AnimatedVisibility(
               visible = showUpButton,
               modifier = Modifier
                   .padding(bottom = 15.dp)
                   .align(Alignment.BottomCenter),
           ) {
               FloatingActionButton(
                   onClick = onScrollUp,
                   modifier= Modifier.size(50.dp).clip(CircleShape),
                   containerColor = MaterialTheme.colorScheme.secondaryContainer,
                   contentColor = MaterialTheme.colorScheme.onPrimary,) {
                   Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "UP")
               }
           }
       }




    }

}

@Preview
@Composable
fun TodoListPreview() {
    MyTodoTheme {
        val lazyColumnListState = rememberLazyListState()
        TodoList(
            lazyColumnListState = lazyColumnListState,
            pagingState = PagingState.Idle,
            todos= listOf(
                Todo(true,54,"Clean House",2,true)
            ),
            onRefresh = { },
            onAddTodo = {},
            showUpButton = true,
            onScrollUp = {},
            onViewTodo = {},
            onDeletedTodo = {},
            onCompletedCheckChange = {_,_-> }
        )
    }

}
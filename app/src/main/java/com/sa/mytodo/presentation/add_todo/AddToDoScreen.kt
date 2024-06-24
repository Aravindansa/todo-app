package com.sa.mytodo.presentation.add_todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sa.mytodo.R
import com.sa.mytodo.presentation.events.TodoUiEvent
import com.sa.mytodo.presentation.components.AppButton
import com.sa.mytodo.presentation.components.CustomAlertDialog
import com.sa.mytodo.presentation.components.DialogLoader
import com.sa.mytodo.presentation.components.ToolbarBack
import com.sa.mytodo.ui.theme.MyTodoTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddToDoScreen(
    navController: NavController,
    viewModel: AddTodoViewModel = hiltViewModel(),
) {
    val todoDesc by viewModel.todoDesc.collectAsState()
    var textFieldValue by remember(todoDesc) {
        mutableStateOf(TextFieldValue(todoDesc, TextRange(todoDesc.length)))
    }
    val isUpdate  by viewModel.isUpdate.collectAsStateWithLifecycle()
    var loader by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(Pair(false,"")) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        viewModel.eventFlow.collectLatest { event ->
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
                    navController.popBackStack()
                }
            }
        }
    }
    if (showError.first){
        CustomAlertDialog(
            dialogText = showError.second,
            onDismissRequest = { showError= Pair(false,"") },
            onConfirmation = {
                showError= Pair(false,"")
                viewModel.submitTodo()
            }
        )
    }
    AddTodo(
        isUpdate = isUpdate,
        todo = textFieldValue,
        loader = loader,
        onTextChange = {
            textFieldValue = it.copy(selection =TextRange(it.text.length))
            viewModel.setTodoDesc(it.text)
        },
        onBack = {
            navController.popBackStack()
        },
        focusRequester = focusRequester,
        onSubmit = {
            viewModel.submitTodo()
        }
    )

}

@Composable
fun AddTodo(
    todo: TextFieldValue,
    isUpdate: Boolean,
    loader:Boolean,
    focusRequester: FocusRequester?=null,
    onTextChange:(TextFieldValue)->Unit,
    onBack: () -> Unit,
    onSubmit:()->Unit
) {
    Surface(modifier = Modifier
        .fillMaxSize()){
        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()) {
                ToolbarBack(
                    title = if(isUpdate) stringResource(R.string.update_todo) else stringResource(R.string.add_new_todo),
                    onClick = onBack
                )
                CustomTextFieldTransparent(
                    modifier=Modifier.weight(1f),
                    value = todo,
                    focusRequester = focusRequester,
                    hint = stringResource(R.string.type_todo),
                    onTextChange =onTextChange,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default,
                )
                AppButton(
                    btnText = if(isUpdate) stringResource(R.string.update_todo) else stringResource(id = R.string.add_todo),
                    enabled = todo.text.isNotEmpty(),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                    onClick = onSubmit
                )

            }
            if (loader){
                DialogLoader()
            }
        }
    }
}

@Preview
@Composable
fun AddTodoPreview() {
    MyTodoTheme {
        AddTodo(
            isUpdate = true,
            todo = TextFieldValue("Todo"),
            loader = false,
            onTextChange = {},
            onBack = {},
            onSubmit = {}
        )

    }

}
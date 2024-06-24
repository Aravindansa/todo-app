package com.sa.mytodo.presentation.todo_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sa.mytodo.R
import com.sa.mytodo.domain.model.Todo
import com.sa.mytodo.ui.theme.MyTodoTheme

@Composable
fun ToDoListItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onDelete: (Todo) -> Unit,
    onView:(Todo)->Unit,
    onCompletedCheckChange:(Boolean, Todo?)->Unit
) {
    Surface(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .clickable { onView(todo) },
        shadowElevation = 2.dp){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.completed?:false,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedColor = MaterialTheme.colorScheme.onPrimary
                ),
                onCheckedChange = { checked ->
                    onCompletedCheckChange(checked, todo)
                }
            )
            Text(text = todo.todoDesc ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier= Modifier
                    .padding(end = 10.dp)
                    .weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                modifier= Modifier
                    .padding(end = 10.dp)
                    .clickable { onDelete(todo) },
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}


@Composable
fun ShowError(modifier: Modifier = Modifier, errorMsg:String?, onRetry:()->Unit) {
    Column(
        modifier = modifier
            .padding(
                vertical = 12.dp,
                horizontal = 15.dp
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = errorMsg?:"",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier= Modifier.padding(horizontal = 15.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Text(text = "Retry",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun Loader(modifier: Modifier=Modifier) {
    CircularProgressIndicator(modifier = modifier.size(60.dp))
}

@Composable
fun PaginateLoading() {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PaginateError(errorMsg:String?,onRetry:()->Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = errorMsg ?: "",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Button(onClick = onRetry,modifier= Modifier.padding(top=15.dp)) {
            Text(text = "Retry",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}


@Preview
@Composable
fun ToDoListItemPreview() {
    MyTodoTheme {
        ToDoListItem(
            todo = Todo(completed = true, 1, "Clean Bath Room", 2),
            onDelete = {},
            onView = {},
            onCompletedCheckChange = { _, _ ->

            }
        )
    }
}
@Preview
@Composable
fun ShowErrorPreview() {
    MyTodoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ){
            ShowError( errorMsg = "No Internet Connection") {

            }
        }

    }

}

@Preview
@Composable
fun PaginateErrorPreview() {
    MyTodoTheme {
        Surface {
            PaginateError(errorMsg = "No Internet Connection") {

            }
        }

    }
}


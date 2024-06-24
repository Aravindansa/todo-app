package com.sa.mytodo.presentation.add_todo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sa.mytodo.ui.theme.MyTodoTheme


@Composable
fun CustomTextFieldTransparent(modifier: Modifier = Modifier,
                               keyboardType: KeyboardType = KeyboardType.Text,
                               imeAction: ImeAction = ImeAction.Default,
                               focusRequester: FocusRequester?=null,
                               hint:String="",
                               value:TextFieldValue,
                               onTextChange: (TextFieldValue) -> Unit) {
    TextField(
        modifier=if (focusRequester==null) modifier else modifier.focusRequester(focusRequester),
        value = value,
        onValueChange ={ onTextChange(it) },
        colors= TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        placeholder = {
            Text(text = hint, style = MaterialTheme.typography.bodyMedium
                .copy(textAlign = TextAlign.Start),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier= Modifier.fillMaxWidth())
        },
        textStyle =  MaterialTheme.typography.bodyMedium
            .copy(color= MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions.Default,
        singleLine = false,
    )
}

@Preview
@Composable
fun CustomTextFieldTransparentPreview() {
    MyTodoTheme {
        Surface {
            CustomTextFieldTransparent(
                modifier=Modifier.fillMaxWidth().height(200.dp),
                value = TextFieldValue("ss"),
                hint = "Type Todo...",
                onTextChange ={
                },
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            )
        }
    }
}

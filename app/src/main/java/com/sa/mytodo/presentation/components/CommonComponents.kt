package com.sa.mytodo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sa.mytodo.R
import com.sa.mytodo.ui.theme.MyTodoTheme

@Composable
fun ToolbarBack(modifier: Modifier=Modifier,title:String,onClick:()->Unit) {
    Row(modifier= modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint=MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(30.dp)
                .clickable { onClick() })
        Text(text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(end = 30.dp),
            textAlign = TextAlign.Center)
    }

}
@Composable
fun CustomAlertDialog(
    dialogTitle: String= stringResource(R.string.alert),
    confirmBtnText:String=stringResource(R.string.retry),
    dismissBtnTxt:String=stringResource(R.string.cancel),
    icon: ImageVector= Icons.Default.Info,
    dialogText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                contentDescription = "Example Icon"
            )
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    confirmBtnText,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    dismissBtnTxt,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun DialogLoader() {
    Box (modifier = Modifier
        .fillMaxSize().clickable {}){
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .size(60.dp))
    }
}
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    btnText: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 12.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor =MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = btnText, style = MaterialTheme.typography.displayMedium)
    }
}
@Preview
@Composable
fun ToolbarBackPreview() {
    MyTodoTheme {
        Surface {
            ToolbarBack(title = "Add Todo") {

            }
        }
    }

}

@Preview
@Composable
fun AppButtonPreview() {
    MyTodoTheme {
        AppButton(btnText = "Submit", enabled = true) {
        }
    }

} 
@Preview
@Composable
fun CustomAlertDialogPreview() {
    MyTodoTheme {
        CustomAlertDialog(
            dialogText = "Something went wrong",
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}








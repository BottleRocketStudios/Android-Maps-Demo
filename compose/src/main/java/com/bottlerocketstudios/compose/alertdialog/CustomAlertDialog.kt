package com.bottlerocketstudios.compose.alertdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottlerocketstudios.compose.utils.Preview

@Composable
fun CustomAlertDialog(modifier: Modifier, title: String, message: String) {
    MaterialTheme {
        Column {
            val dialogVisibility = remember {
                mutableStateOf(value = true)
            }

            AlertDialog(
                onDismissRequest = {
                    dialogVisibility.value = false
                },
                title = {
                    Text(text = title)
                }, text = {
                    Text(text = message)
                },
                confirmButton = {
                    CustomAlertDialogConfirmButton(dialogVisibility = dialogVisibility)
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewCustomAlertDialog() {
    Preview {
        CustomAlertDialog(
            modifier = Modifier,
            title = "Error",
            message = "Unable to access the internet"
        )
    }
}

@Composable
fun CustomAlertDialogConfirmButton(dialogVisibility: MutableState<Boolean>) {
    Button(onClick = { dialogVisibility.value = false }
    ) {
        Text("OK")
    }
}


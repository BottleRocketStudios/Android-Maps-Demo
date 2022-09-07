package com.bottlerocketstudios.compose.alertdialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.bottlerocketstudios.compose.R
import com.bottlerocketstudios.compose.utils.Preview

@Composable
fun CustomAlertDialog(title: Int, message: Int, onDismiss: () -> Unit) {
    MaterialTheme {
        Column {

            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(text = stringResource(id = title))
                }, text = {
                Text(text = stringResource(id = message))
            },
                confirmButton = {
                    CustomAlertDialogConfirmButton(onDismiss = onDismiss)
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                )
            )
        }
    }
}

@Composable
@Preview
fun PreviewCustomAlertDialog() {
    Preview {
        CustomAlertDialog(
            title = R.string.test_general_error_title,
            message = R.string.test_api_error_description,
            onDismiss = {}
        )
    }
}

@Composable
fun CustomAlertDialogConfirmButton(onDismiss: () -> Unit) {
    Button(
        onClick = onDismiss
    ) {
        Text("OK")
    }
}

package com.fsacchi.mynotes.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fsacchi.mynotes.R


@Composable
fun AlertDialog(message: String, ok: () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }
    AlertDialog(
        onDismissRequest = {
        },

        title = {
            Text(
                text = stringResource(id = R.string.warning),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(text = message, fontSize = 16.sp)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    ok()
                }) {
                Text(
                    text = stringResource(id = R.string.ok),
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(color = Color.Black)
                )
            }
        }
    )

}
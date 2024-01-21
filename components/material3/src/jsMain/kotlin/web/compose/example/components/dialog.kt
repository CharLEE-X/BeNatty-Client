package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeTitle
import web.compose.material3.buttons.OutlinedButton
import web.compose.material3.dialog.Dialog

@Composable
fun DialogShowcase() {
    LargeTitle("Dialog")
    var dialogOpen by remember { mutableStateOf(false) }
    var dialogClosing by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { dialogOpen = !dialogOpen }
    ) {
        Text("Open dialog")
    }

    if (dialogOpen || dialogClosing) {
        Dialog(
            open = dialogOpen && !dialogClosing,
            onClosed = {
                dialogOpen = false
                dialogClosing = false
            },
            onClosing = {
                dialogClosing = true
            },
            headline = {
                Text("Dialog header")
            },
            actions = {
                Text("Dialog actions")
                OutlinedButton(
                    onClick = {
                        dialogClosing = true
                    }
                ) {
                    Text("Close")
                }
            }
        ) {
            Text("This is a dialog content")
        }
    }
}

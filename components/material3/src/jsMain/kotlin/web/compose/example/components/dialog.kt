package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeTitle
import web.compose.material3.buttons.OutlinedButton
import web.compose.material3.dialog.Dialog
import web.compose.material3.divider.Divider
import web.compose.material3.slot

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
        ) {
            Span({ slot = "headline" }) { Text("Dialog") }
            Span({ slot = "body" }) { Text("This is a dialog") }
            Divider()
            OutlinedButton(
                onClick = {
                    console.log(it)
                    dialogClosing = true
                },
                modifier = Modifier.attrsModifier {
                    slot = "footer"
                }
            ) {
                Text("Close")
            }
        }
    }
}

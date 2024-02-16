package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em

@Composable
fun DeleteButtonWithConfirmation(
    deleteText: String,
    cancelText: String,
    onDelete: () -> Unit,
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (!showDeleteConfirmation) {
        AppFilledButton(
            onClick = { showDeleteConfirmation = true },
            leadingIcon = { MdiAdd() },
        ) {
            SpanText(text = deleteText)
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(1.em)
        ) {
            AppFilledTonalButton(
                onClick = {
                    onDelete()
                    showDeleteConfirmation = false
                },
                leadingIcon = { MdiDelete() },
            ) {
                SpanText(text = deleteText)
            }
            AppFilledButton(
                onClick = { showDeleteConfirmation = false },
                leadingIcon = { MdiCancel() },
            ) {
                SpanText(text = cancelText)
            }
        }
    }
}

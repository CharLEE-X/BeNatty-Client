package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px


@Composable
fun EditCancelButton(
    isEditing: Boolean,
    editText: String,
    cancelText: String,
    edit: () -> Unit,
    cancel: () -> Unit,
    width: Int = 150,
) {
    AppFilledButton(
        onClick = { if (isEditing) cancel() else edit() },
        leadingIcon = { if (isEditing) MdiCancel() else MdiEdit() },
        modifier = Modifier.width(width.px),
    ) {
        SpanText(if (isEditing) cancelText else editText)
    }
}

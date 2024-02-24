package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Switch

@Composable
fun SwitchSection(
    title: String,
    selected: Boolean,
    errorText: String? = null,
    disabled: Boolean = false,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick { onClick() }
                .cursor(Cursor.Pointer)
        ) {
            Switch(
                selected = selected,
                disabled = disabled,
            )
            SpanText(text = title)
        }
        errorText?.let {
            SpanText(
                text = title,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodySmall)
                    .color(MaterialTheme.colors.error.value())
            )
        }
    }
}

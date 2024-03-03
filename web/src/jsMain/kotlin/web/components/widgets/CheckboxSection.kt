package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Checkbox

@Composable
fun CheckboxSection(
    title: String,
    selected: Boolean,
    errorText: String? = null,
    disabled: Boolean = false,
    onClick: () -> Unit,
) {
    val opacity = if (disabled) 0.5f else 1f

    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick {
                    if (!disabled) {
                        onClick()
                    }
                }
                .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
        ) {
            Checkbox(
                checked = selected,
                disabled = disabled,
            )
            SpanText(
                text = title,
                modifier = Modifier
                    .color(MaterialTheme.colors.onSurface)
                    .opacity(opacity)
                    .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
        errorText?.let {
            SpanText(
                text = title,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodySmall)
                    .color(MaterialTheme.colors.error)
            )
        }
    }
}

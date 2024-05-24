package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.forms.Checkbox
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import web.SubtitleStyle

@Composable
fun CheckboxSection(
    title: String,
    selected: Boolean,
    errorText: String? = null,
    disabled: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    val opacity = if (disabled) 0.5f else 1f

    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
        ) {
            Checkbox(
                checked = selected,
                enabled = !disabled,
                onCheckedChange = onClick
            )
            SpanText(
                text = title,
                modifier = Modifier
                    .opacity(opacity)
                    .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
        errorText?.let {
            SpanText(
                text = title,
                modifier = SubtitleStyle.toModifier()
                    .color(Colors.Red)
            )
        }
    }
}

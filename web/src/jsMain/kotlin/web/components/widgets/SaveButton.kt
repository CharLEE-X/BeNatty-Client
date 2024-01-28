package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.compose.material3.component.FilledButton


@Composable
fun SaveButton(
    text: String,
    disabled: Boolean,
    onClick: () -> Unit,
) {
    FilledButton(
        disabled = disabled,
        onClick = { onClick() },
        modifier = Modifier
            .margin(top = 1.em)
            .width(200.px)
    ) {
        SpanText(
            text = text,
            modifier = Modifier.margin(0.5.em)
        )
    }
}

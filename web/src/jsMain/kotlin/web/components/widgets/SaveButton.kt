package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.util.onEnterKeyDown

@Composable
fun SaveButton(
    text: String,
    disabled: Boolean,
    onClick: () -> Unit,
) {
    AppFilledButton(
        disabled = disabled,
        onClick = { onClick() },
        modifier = Modifier
            .margin(top = 1.em)
            .width(200.px)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        SpanText(
            text = text,
            modifier = Modifier.margin(0.5.em)
        )
    }
}

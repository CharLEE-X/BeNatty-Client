package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.SubtitleStyle

@Composable
fun SwitchSection(
    title: String,
    selected: Boolean,
    errorText: String? = null,
    disabled: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(1.em)
        ) {
            Switch(
                checked = selected,
                enabled = !disabled,
                onCheckedChange = onClick
            )
            SpanText(text = title)
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

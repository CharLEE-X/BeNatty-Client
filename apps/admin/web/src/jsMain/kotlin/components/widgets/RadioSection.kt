package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.RadioInput

@Composable
fun RadioSection(
    title: String,
    values: List<String>,
    selected: String,
    onClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        SpanText(text = title)
        values.forEach { value ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(left = 1.em)
                    .gap(1.em)
                    .cursor(Cursor.Pointer)
                    .onClick { onClick(value) }
            ) {
                RadioInput(
                    checked = selected == value,
                    attrs = Modifier.toAttrs()
                )
                SpanText(text = value.enumCapitalized()) // TODO: Localize
            }
        }
    }
}

package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.compose.material3.component.OutlinedButton

@Composable
fun NoItemsListAction(
    pressText: String,
    createText: String,
    toStartText: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.gap(0.5.em)
    ) {
        SpanText(text = pressText)
        OutlinedButton(
            onClick = { onClick() },
        ) {
            SpanText(text = createText.lowercase())
        }
        SpanText(text = toStartText.lowercase())
    }
}

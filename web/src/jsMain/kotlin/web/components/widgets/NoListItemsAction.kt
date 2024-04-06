package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.util.onEnterKeyDown

@Composable
fun NoItemsListAction(
    modifier: Modifier,
    pressText: String,
    createText: String,
    toStartText: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(0.5.em)
    ) {
        SpanText(text = pressText)
        AppOutlinedButton(
            onClick = { onClick() },
            modifier = Modifier
                .tabIndex(0)
                .onEnterKeyDown(onClick)
        ) {
            SpanText(text = createText.lowercase())
        }
        SpanText(text = toStartText.lowercase())
    }
}

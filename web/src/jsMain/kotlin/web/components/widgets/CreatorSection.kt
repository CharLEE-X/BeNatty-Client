package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em


@Composable
fun CreatorSection(
    title: String,
    creatorName: String,
    onClick: () -> Unit,
    afterTitle: @Composable (() -> Unit) = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.gap(1.em)
    ) {
        SpanText(text = title)
        afterTitle()
        AppOutlinedButton(
            onClick = { onClick() },
        ) {
            SpanText(text = creatorName)
        }
    }
}

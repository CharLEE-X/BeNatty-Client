package web.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Span

@Composable
fun Footer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(topBottom = 1.5.cssRem, leftRight = 10.percent),
        contentAlignment = Alignment.Center,
    ) {
        Span(
            attrs = Modifier.textAlign(TextAlign.Center).toAttrs(),
        ) {
            SpanText("Built with ")
            Link(
                path = "https://github.com/varabyte/kobweb",
                text = "Kobweb",
                variant = UncoloredLinkVariant,
            )
            SpanText(", template designed by ")

            Link(
                "https://ui-rocket.com",
                "UI Rocket",
                variant = UncoloredLinkVariant,
            )
        }
    }
}

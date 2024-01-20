package components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.style.vars.color.ColorVar
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Span
import theme.toSitePalette

@Composable
fun Footer(modifier: Modifier = Modifier) {
    val colorMode by ColorMode.currentState

    Box(
        modifier = modifier
            .backgroundColor(colorMode.toSitePalette().nearBackground)
            .padding(topBottom = 1.5.cssRem, leftRight = 10.percent),
        contentAlignment = Alignment.Center,
    ) {
        Span(
            attrs = Modifier.textAlign(TextAlign.Center).toAttrs(),
        ) {
            val sitePalette = ColorMode.current.toSitePalette()
            SpanText("Built with ")
            Link(
                path = "https://github.com/varabyte/kobweb",
                text = "Kobweb",
                modifier = Modifier.setVariable(ColorVar, sitePalette.primary),
                variant = UncoloredLinkVariant,
            )
            SpanText(", template designed by ")

            Link(
                "https://ui-rocket.com",
                "UI Rocket",
                Modifier.setVariable(ColorVar, sitePalette.secondary).whiteSpace(WhiteSpace.NoWrap),
                variant = UncoloredLinkVariant,
            )
        }
    }
}

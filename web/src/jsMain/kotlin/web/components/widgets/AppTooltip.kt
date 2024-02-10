package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.silk.components.overlay.AdvancedTooltip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme

@Composable
fun AppTooltip(text: String) {
    AdvancedTooltip(
        text = text,
        target = ElementTarget.PreviousSibling,
        showDelayMs = 500,
        hasArrow = false,
        modifier = Modifier
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceVariant.value())
            .border(
                width = 1.px,
                color = MaterialTheme.colors.mdSysColorOnSurface.value(),
                style = LineStyle.Solid
            )
    )
}

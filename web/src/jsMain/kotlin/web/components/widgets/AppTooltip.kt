package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.overlay.AdvancedTooltip
import com.varabyte.kobweb.silk.components.overlay.TooltipVars
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme

@Composable
fun AppTooltip(text: String) {
    AdvancedTooltip(
        text = text,
        target = ElementTarget.PreviousSibling,
        showDelayMs = 500,
        hasArrow = false,
        modifier = Modifier
            .border(
                width = 1.px,
                color = MaterialTheme.colors.onSurface,
                style = LineStyle.Solid
            )
            .setVariable(TooltipVars.BackgroundColor, MaterialTheme.colors.surfaceVariant)
            .setVariable(TooltipVars.Color, MaterialTheme.colors.onSurface)
            .zIndex(1000)
    )
}

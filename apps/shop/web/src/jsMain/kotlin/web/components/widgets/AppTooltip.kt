package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.overlay.AdvancedTooltip
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

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
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
//            .setVariable(TooltipVars.BackgroundColor, ColorMode.current.toPalette().background)
//            .setVariable(TooltipVars.Color, ColorMode.current.toPalette().color)
            .zIndex(1000)
    )
}

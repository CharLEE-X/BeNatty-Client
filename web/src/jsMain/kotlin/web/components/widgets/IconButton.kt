package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import web.util.onEnterKeyDown

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    iconColor: Color = ColorMode.current.toPalette().color,
    icon: @Composable () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Span(
        modifier
            .color(iconColor)
            .fontSize(1.5.em)
            .margin(0.25.em)
            .disabled(!enabled)
            .thenIf(enabled, Modifier
                .tabIndex(0)
                .onClick { onClick() }
                .onEnterKeyDown { onClick() }
                .onMouseOver { hovered = true }
                .onMouseOut { hovered = false }
                .onFocusIn { hovered = true }
                .onFocusOut { hovered = false }
                .tabIndex(0)
                .cursor(Cursor.Pointer)
                .opacity(if (hovered) 1f else 0.6f)
                .scale(if (hovered) 1.1f else 1f)
                .transition(
                    CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                )
            )
            .thenIf(!enabled, Modifier.opacity(0.6f))
            .toAttrs()
    ) {
        icon()
    }
}

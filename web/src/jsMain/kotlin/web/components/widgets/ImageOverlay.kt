package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s

@Composable
fun ImageOverlay(
    modifier: Modifier = Modifier,
    shadowColor: Color,
    overlayColor: Color,
    hovered: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .backgroundColor(shadowColor.toRgb().copy(alpha = 40))
            .boxShadow(
                offsetX = 0.px,
                offsetY = 0.px,
                blurRadius = 80.px,
                spreadRadius = 0.px,
                color = overlayColor.toRgb().copy(alpha = 120),
                inset = true
            )
            .thenIf(hovered) { Modifier.scale(1.05) }
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    )
}

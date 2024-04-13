package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme

@Composable
fun RotatableChevron(
    modifier: Modifier = Modifier,
    hovered: Boolean = false,
    open: Boolean = false,
    color: CSSColorValue = MaterialTheme.colors.onSurface,
) {
    MdiChevronLeft(
        modifier = modifier
            .rotate(if (open) 90.deg else 270.deg)
            .color(color)
            .fontWeight(if (hovered) FontWeight.Bold else FontWeight.Normal)
            .transition(
                CSSTransition("rotate", 0.3.s, TransitionTimingFunction.Ease),
            )
    )
}

package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.s

@Composable
fun RotatableChevron(
    modifier: Modifier = Modifier,
    hovered: Boolean = false,
    open: Boolean = false,
    initialRotate: Int = 90,
    color: CSSColorValue = ColorMode.current.toPalette().color,
) {
    MdiChevronLeft(
        modifier = modifier
            .rotate(if (open) initialRotate.deg else (-180 - initialRotate).deg)
            .color(color)
            .fontWeight(if (hovered) FontWeight.Bold else FontWeight.Normal)
            .userSelect(UserSelect.None)
            .draggable(false)
            .transition(
                Transition.of("rotate", 0.2.s, TransitionTimingFunction.Ease),
                Transition.of("color", 0.2.s, TransitionTimingFunction.Ease),
            )
    )
}

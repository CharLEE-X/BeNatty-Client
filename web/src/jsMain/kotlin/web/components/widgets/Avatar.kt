package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    url: String,
    shape: Shape = Shape.Circle,
    isClickEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    val borderRadius = when (shape) {
        Shape.Circle -> 50.percent
        Shape.Square -> 20.px
    }

    Image(
        src = url,
        modifier = AvatarStyle.toModifier().then(modifier)
            .borderRadius(borderRadius)
            .onClick {
                if (isClickEnabled) {
                    onClick()
                }
            }
    )
}

enum class Shape {
    Circle,
    Square,
}

val AvatarStyle by ComponentStyle {
    base {
        Modifier
            .cursor(Cursor.Pointer)
            .transition(CSSTransition("scale", 0.15.s, TransitionTimingFunction.Ease))
    }
    hover {
        Modifier.scale(1.05)
    }
    Breakpoint.ZERO {
        Modifier.size(24.px)
    }
    Breakpoint.SM {
        Modifier.size(32.px)
    }
    Breakpoint.MD {
        Modifier.size(48.px)
    }
    Breakpoint.LG {
        Modifier.size(56.px)
    }
    Breakpoint.XL {
        Modifier.size(144.px)
    }
}

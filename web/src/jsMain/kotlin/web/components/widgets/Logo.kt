package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.HeadlineTextStyle
import web.HeadlineTextStyleBold
import web.HeadlineTextStyleLight
import web.util.onEnterKeyDown

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    hasLogo: Boolean = false,
    hasText: Boolean = true,
    logoSize: CSSLengthOrPercentageNumericValue = 2.em,
    color: CSSColorValue = MaterialTheme.colors.onSurface,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .gap(1.em)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .scale(if (hovered) 1.05 else 1.0)
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        if (hasLogo) {
            Image(
                src = "/logo.png",
                description = "",
                modifier = Modifier
                    .size(logoSize)
                    .display(DisplayStyle.Block)
            )
        }
        if (hasText) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.color(color)
            ) {
                SpanText(
                    text = "BE",
                    modifier = HeadlineTextStyle.toModifier(HeadlineTextStyleBold)
                )
                SpanText(
                    text = "NATTY",
                    modifier = HeadlineTextStyle.toModifier(HeadlineTextStyleLight)
                )
            }
        }
    }
}

package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowBackIosNew
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s

@Composable
fun Breadcrumbs(
    modifier: Modifier = Modifier,
    urls: List<String>,
    onClick: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(8.px)
    ) {
        urls.forEachIndexed { index, name ->
            if (index != 0) {
                MdiArrowBackIosNew(
                    modifier = Modifier
                        .rotate(180.deg)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.SemiBold)
                        .whiteSpace(WhiteSpace.NoWrap)
                )
            }
            SpanText(
                text = name,
                modifier = BreadcrumbStyle.toModifier()
                    .onClick { onClick(index) }
            )
        }
    }
}

val BreadcrumbStyle by ComponentStyle {
    base {
        Modifier
            .cursor(Cursor.Pointer)
            .transition(CSSTransition("textDecorationLine", 0.15.s, TransitionTimingFunction.Ease))
    }
    hover {
        Modifier.textDecorationLine(TextDecorationLine.Underline)
    }
}

package web.util

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme

fun Modifier.onEnterKeyDown(block: () -> Unit) = attrsModifier {
    onKeyDown { if (it.key == "Enter") block() }
}

@Composable
fun Modifier.glossy(
    color: Color = MaterialTheme.colors.surface,
    alpha: Int = 70,
    blur: CSSLengthNumericValue = 60.px,
    cornerRadius: CSSLengthNumericValue = 12.px
): Modifier = this
    .borderRadius(cornerRadius)
    .backgroundColor(color.toRgb().copy(alpha = alpha))
    .backdropFilter(blur(blur))
    .border(
        width = 1.px,
        color = MaterialTheme.colors.surface,
        style = LineStyle.Solid,
    )

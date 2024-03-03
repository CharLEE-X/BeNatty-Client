package web.util

import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import org.jetbrains.compose.web.css.px

fun Modifier.onEnterKeyDown(block: () -> Unit) = attrsModifier {
    onKeyDown { if (it.key == "Enter") block() }
}

fun Modifier.glossy(
    color: Color,
    alpha: Int = 70,
    blur: CSSLengthNumericValue = 60.px
) = this
    .backgroundColor(color.toRgb().copy(alpha = alpha))
    .backdropFilter(blur(blur))

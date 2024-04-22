package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.compose.material3.component.FilledTextField

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onEnterPress: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = { MdiSearch() },
    trailingIcon: @Composable (() -> Unit)? = null,
    containerShape: CSSLengthOrPercentageNumericValue = 30.px,
    containerColor: CSSColorValue = MaterialTheme.colors.surfaceContainerHighest,
    unFocusedOutlineColor: CSSColorValue = MaterialTheme.colors.surface,
    focusedOutlineColor: CSSColorValue = MaterialTheme.colors.inverseSurface,
    hoverOutlineColor: CSSColorValue = focusedOutlineColor,
) {
    var focused by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }

    val borderColor = when {
        hovered -> hoverOutlineColor
        else -> unFocusedOutlineColor
    }
    val borderWidth = if (focused || hovered) 2.px else 1.px

    Box(
        modifier = Modifier
            .backgroundColor(containerColor)
            .borderRadius(containerShape)
            .border(
                width = borderWidth,
                color = borderColor,
                style = LineStyle.Solid,
            )
            .transition(
                CSSTransition("border-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        FilledTextField(
            value = value,
            onInput = onValueChange,
            label = placeholder,
            placeholder = placeholder,
            autoComplete = AutoComplete.off,
            containerColor = Colors.Transparent,
            focusActiveIndicatorColor = Colors.Transparent,
            hoverActiveIndicatorColor = Colors.Transparent,
            activeIndicatorColor = Colors.Transparent,
            hoverStateLayerColor = Colors.Transparent,
            labelTextLineHeight = 20.px,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = modifier
                .onKeyDown { if (it.key == "Enter") onEnterPress() }
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { focused = true }
                .onFocusOut { focused = false }
        )
    }
}

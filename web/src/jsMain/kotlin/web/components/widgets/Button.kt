package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.compose.material3.component.ElevatedButton
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.TextButton
import web.util.onEnterKeyDown

@Composable
fun AppElevatedButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = 0.px,
        labelTextFont = labelTextFont,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppFilledButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    disabledContainerColor: CSSColorValue? = null,
    cornerRadius: CSSLengthOrPercentageNumericValue? = 0.px,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    FilledButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        disabledContainerColor = disabledContainerColor,
        containerShape = cornerRadius,
        labelTextFont = labelTextFont,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppOutlinedButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = 0.px,
        labelTextFont = labelTextFont,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppFilledTonalButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = 0.px,
        labelTextFont = labelTextFont,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppTextButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        labelTextColor = labelTextColor,
        labelTextFont = labelTextFont,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun CreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer()
        AppFilledButton(
            onClick = onClick,
            leadingIcon = { MdiCreate() },
            modifier = modifier
                .width(150.px)
                .tabIndex(0)
                .onEnterKeyDown(onClick)
        ) {
            SpanText(getString(Strings.Create).uppercase())
        }
    }
}

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    SpanText(
        text = title,
        modifier = modifier
            .padding(
                leftRight = 16.px,
                topBottom = 8.px
            )
            .onMouseOver { if (enabled) hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { if (enabled) hovered = true }
            .onFocusOut { hovered = false }
            .onClick { onClick() }
            .thenIf(enabled, Modifier.tabIndex(0))
            .onEnterKeyDown { if (enabled) onClick() }
            .backgroundColor(if (hovered) MaterialTheme.colors.primary else MaterialTheme.colors.primary)
            .transition(
                CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease),
            )
    )
}

package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import web.compose.material3.component.ElevatedButton
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.TextButton

@Composable
fun AppElevatedButton(
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        modifier = modifier,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape ?: 12.px,
        labelTextFont = labelTextFont,
        content = content
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
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    FilledButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        modifier = modifier,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        disabledContainerColor = disabledContainerColor,
        containerShape = containerShape ?: 12.px,
        labelTextFont = labelTextFont,
        content = content
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
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        modifier = modifier,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape ?: 12.px,
        labelTextFont = labelTextFont,
        content = content
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
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        onClick = { onClick() },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        modifier = modifier,
        containerColor = containerColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape ?: 12.px,
        labelTextFont = labelTextFont,
        content = content
    )
}

@Composable
fun AppTextButton(
    onClick: (SyntheticMouseEvent) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        disabled = disabled,
        modifier = modifier,
        labelTextColor = labelTextColor,
        labelTextFont = labelTextFont,
        content = content
    )
}

package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
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
            modifier = modifier.width(150.px)
        ) {
            SpanText(getString(Strings.Create).uppercase())
        }
    }
}

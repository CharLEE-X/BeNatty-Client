package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun MdButtonTagElement(
    name: String,
    onClick: (SyntheticMouseEvent) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier,
    content: @Composable RowScope.() -> Unit
) {
    MdTagElement(
        tagName = "md-$name-button",
        applyAttrs = modifier
            .onClick { evt ->
                onClick(evt)
                evt.stopPropagation()
            }
            .toAttrs {
                classes("md-button")
                trailingIcon?.let { attr("hasIcon", "") }
                if (disabled) attr("disabled", "")
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                it()
                Box(Modifier.size(8.px))
            }
            content()
            trailingIcon?.let {
                Box(Modifier.size(8.px))
                it()
            }
        }
    }.also { jsRequire("@material/web/button/$name-button.js") }
}

@Composable
fun ElevatedButton(
    onClick: (SyntheticMouseEvent) -> Unit,
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
    val tag = "elevated"
    MdButtonTagElement(
        name = tag,
        onClick = onClick,
        disabled = disabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier
            .styleModifier {
                containerColor?.let { property("--md-$tag-button-container-color", it.toString()) }
                labelTextColor?.let { property("--md-$tag-button-label-text-color", it.toString()) }
                containerShape?.let { property("--md-$tag-button-container-shape", it.toString()) }
                labelTextFont?.let { property("--md-$tag-button-label-text-font", it) }
            },
        content = content
    )
}

@Composable
fun FilledButton(
    onClick: (SyntheticMouseEvent) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    containerColor: CSSColorValue? = null,
    disabledContainerColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    labelTextFont: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    val tag = "filled"
    MdButtonTagElement(
        name = tag,
        onClick = onClick,
        disabled = disabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.styleModifier {
            containerColor?.let { property("--md-$tag-button-container-color", it.toString()) }
            disabledContainerColor?.let { property("--md-$tag-button-disabled-container-color", it.toString()) }
            labelTextColor?.let { property("--md-$tag-button-label-text-color", it.toString()) }
            containerShape?.let { property("--md-$tag-button-container-shape", it.toString()) }
            labelTextFont?.let { property("--md-$tag-button-label-text-font", it) }
        },
        content = content
    )
}

@Composable
fun OutlinedButton(
    onClick: (SyntheticMouseEvent) -> Unit,
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
    val tag = "outlined"
    MdButtonTagElement(
        name = tag,
        onClick = onClick,
        disabled = disabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.styleModifier {
            containerColor?.let { property("--md-$tag-button-outline-color", it.toString()) }
            labelTextColor?.let { property("--md-$tag-button-label-text-color", it.toString()) }
            containerShape?.let { property("--md-$tag-button-container-shape", it.toString()) }
            labelTextFont?.let { property("--md-$tag-button-label-text-font", it) }
        },
        content = content
    )
}

@Composable
fun FilledTonalButton(
    onClick: (SyntheticMouseEvent) -> Unit,
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
    val tag = "filled-tonal"
    MdButtonTagElement(
        name = tag,
        onClick = onClick,
        disabled = disabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.styleModifier {
            containerColor?.let { property("--md-$tag-button-outline-color", it.toString()) }
            labelTextColor?.let { property("--md-$tag-button-label-text-color", it.toString()) }
            containerShape?.let { property("--md-$tag-button-container-shape", it.toString()) }
            labelTextFont?.let { property("--md-$tag-button-label-text-font", it) }
        },
        content = content
    )
}

@Composable
fun TextButton(
    onClick: (SyntheticMouseEvent) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
    labelTextColor: CSSColorValue? = null,
    labelTextFont: String? = null,
    hoverContainerColor: CSSColorValue? = null,
    content: @Composable RowScope.() -> Unit
) {
    val tag = "text"
    MdButtonTagElement(
        name = "text",
        onClick = onClick,
        disabled = disabled,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        modifier = modifier.styleModifier {
            labelTextColor?.let { property("--md-$tag-button-label-text-color", it.toString()) }
            labelTextFont?.let { property("--md-$tag-button-label-text-font", it) }
            hoverContainerColor?.let { property("--md-$tag-button-hover-state-layer-color", it) }
        },
        content = content
    )
}

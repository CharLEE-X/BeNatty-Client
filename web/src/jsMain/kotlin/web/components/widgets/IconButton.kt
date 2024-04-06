package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSPercentageNumericValue
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.Width
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.component.FilledIconButton
import web.compose.material3.component.FilledTonalIconButton
import web.compose.material3.component.IconButton
import web.compose.material3.component.OutlinedIconButton
import web.util.onEnterKeyDown

@Composable
fun AppIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    iconColor: CSSColorValue? = null,
    stateLayerShape: String? = null,
    iconSize: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    IconButton(
        toggle = toggle,
        selected = selected,
        disabled = disabled,
        onClick = { onClick() },
        iconColor = iconColor,
        stateLayerShape = stateLayerShape ?: "12px",
        iconSize = iconSize,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppFilledIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue? = null,
    buttonContainerShape: CSSLengthOrPercentageNumericValue? = null,
    buttonContainerWidth: Width? = null,
    buttonContainerHeight: Height? = null,
    iconSize: CSSPercentageNumericValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    FilledIconButton(
        toggle = toggle,
        selected = selected,
        disabled = disabled,
        onClick = { onClick() },
        selectedContainerColor = selectedContainerColor,
        buttonContainerShape = buttonContainerShape ?: 12.px,
        buttonContainerWidth = buttonContainerWidth,
        buttonContainerHeight = buttonContainerHeight,
        iconSize = iconSize,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppOutlinedIconButton(
    modifier: Modifier = Modifier,
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: () -> Unit = {},
    outlineColor: CSSColorValue? = null,
    outlineWidth: CSSLengthOrPercentageNumericValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    containerWidth: Width? = null,
    containerHeight: Height? = null,
    iconSize: CSSLengthOrPercentageNumericValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    OutlinedIconButton(
        toggle = toggle,
        selected = selected,
        disabled = disabled,
        onClick = { onClick() },
        outlineColor = outlineColor,
        outlineWidth = outlineWidth,
        containerShape = containerShape ?: 12.px,
        containerWidth = containerWidth,
        containerHeight = containerHeight,
        iconSize = iconSize,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
fun AppFilledTonalIconButton(
    modifier: Modifier = Modifier,
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: () -> Unit = {},
    containerColor: CSSColorValue? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    containerWidth: Width? = null,
    containerHeight: Height? = null,
    iconSize: CSSLengthOrPercentageNumericValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    FilledTonalIconButton(
        toggle = toggle,
        selected = selected,
        disabled = disabled,
        onClick = { onClick() },
        containerColor = containerColor,
        containerShape = containerShape ?: 12.px,
        containerWidth = containerWidth,
        containerHeight = containerHeight,
        iconSize = iconSize,
        content = content,
        modifier = modifier
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

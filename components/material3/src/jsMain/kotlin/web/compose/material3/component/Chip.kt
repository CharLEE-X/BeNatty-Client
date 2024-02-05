@file:Suppress("UnsafeCastFromDynamic")

package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
private fun BaseChip(
    label: String,
    elevated: Boolean = false,
    disabled: Boolean = false,
    alwaysFocusable: Boolean = false,
    tagName: String,
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue?,
    outlineColor: CSSColorValue?,
    labelTextColor: CSSColorValue?,
    containerShape: String?,
    iconSize: String?,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = tagName,
    applyAttrs = modifier
        .onClick { onClick(it) }
        .styleModifier {
            selectedContainerColor?.let { property("--md-filter-chip-selected-container-color", it.toString()) }
            outlineColor?.let { property("--md-outlined-text-field-outline-color", it.toString()) }
            labelTextColor?.let { property("--md-filter-chip-label-text-color", it.toString()) }
            containerShape?.let { property("--md-outlined-text-field-container-shape", it) }
            iconSize?.let { property("--md-filter-chip-icon-size", it) }
        }
        .toAttrs {
            if (elevated) attr("elevated", "")
            if (disabled) attr("disabled", "")
            if (alwaysFocusable) attr("alwaysFocusable", "")
            attr("label", label)
        },
    content = content
)

@Composable
fun InputChip(
    label: String,
    avatar: Boolean = false,
    removeOnly: Boolean = false,
    elevated: Boolean = false,
    disabled: Boolean = false,
    alwaysFocusable: Boolean = false,
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue? = null,
    outlineColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: String? = null,
    iconSize: String? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    content: ContentBuilder<MdElement>? = null
) {
    BaseChip(
        tagName = "md-input-chip",
        onClick = onClick,
        content = content,
        label = label,
        elevated = elevated,
        disabled = disabled,
        alwaysFocusable = alwaysFocusable,
        selectedContainerColor = selectedContainerColor,
        outlineColor = outlineColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape,
        iconSize = iconSize,
        modifier = modifier.attrsModifier {
            if (avatar) attr("avatar", "")
            if (removeOnly) attr("removeOnly", "")
        }
    ).also { jsRequire("@material/web/chips/input-chip.js") }
}

@Composable
fun SuggestionChip(
    label: String,
    elevated: Boolean = false,
    disabled: Boolean = false,
    alwaysFocusable: Boolean = false,
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue? = null,
    outlineColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: String? = null,
    iconSize: String? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    content: ContentBuilder<MdElement>? = null
) {
    BaseChip(
        tagName = "md-suggestion-chip",
        modifier = modifier,
        onClick = onClick,
        content = content,
        label = label,
        elevated = elevated,
        disabled = disabled,
        alwaysFocusable = alwaysFocusable,
        selectedContainerColor = selectedContainerColor,
        outlineColor = outlineColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape,
        iconSize = iconSize,
    ).also { jsRequire("@material/web/chips/suggestion-chip.js") }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean = false,
    removable: Boolean = false,
    elevated: Boolean = false,
    disabled: Boolean = false,
    alwaysFocusable: Boolean = false,
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue? = null,
    outlineColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: String? = null,
    iconSize: String? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    content: ContentBuilder<MdElement>? = null
) {
    BaseChip(
        tagName = "md-filter-chip",
        onClick = onClick,
        content = content,
        label = label,
        elevated = elevated,
        disabled = disabled,
        alwaysFocusable = alwaysFocusable,
        selectedContainerColor = selectedContainerColor,
        outlineColor = outlineColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape,
        iconSize = iconSize,
        modifier = modifier.attrsModifier {
            if (removable) attr("removable", "")
            if (selected) attr("selected", "")
        }
    ).also { jsRequire("@material/web/chips/filter-chip.js") }
}

@Composable
fun AssistChip(
    label: String,
    elevated: Boolean = false,
    disabled: Boolean = false,
    alwaysFocusable: Boolean = false,
    modifier: Modifier = Modifier,
    selectedContainerColor: CSSColorValue? = null,
    outlineColor: CSSColorValue? = null,
    labelTextColor: CSSColorValue? = null,
    containerShape: String? = null,
    iconSize: String? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    content: ContentBuilder<MdElement>? = null
) {
    BaseChip(
        tagName = "md-assist-chip",
        modifier = modifier,
        onClick = onClick,
        content = content,
        label = label,
        elevated = elevated,
        disabled = disabled,
        alwaysFocusable = alwaysFocusable,
        selectedContainerColor = selectedContainerColor,
        outlineColor = outlineColor,
        labelTextColor = labelTextColor,
        containerShape = containerShape,
        iconSize = iconSize
    ).also { jsRequire("@material/web/chips/assist-chip.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun ChipSet(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-chip-set",
    applyAttrs = modifier.toAttrs {
    },
    content = content
).also { jsRequire("@material/web/chips/chip-set.js") }

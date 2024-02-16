package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
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
private fun BaseCheckbox(
    tag: String,
    modifier: Modifier,
    value: Boolean?,
    onClick: (SyntheticMouseEvent) -> Unit,
    checked: Boolean,
    indeterminate: Boolean?,
    disabled: Boolean = false,
    containerShape: CSSLengthOrPercentageNumericValue?,
    containerSize: CSSLengthOrPercentageNumericValue?,
    outlineColor: CSSColorValue?,
    outlineWidth: CSSLengthOrPercentageNumericValue?,
    selectedContainerColor: CSSColorValue?,
    content: ContentBuilder<MdElement>?
) {
    MdTagElement(
        tagName = "md-$tag",
        applyAttrs = modifier
            .onClick { onClick(it) }
            .styleModifier {
                containerShape?.let { property("--md-$tag-container-shape", it.toString()) }
                containerSize?.let { property("--md-$tag-container-size", it.toString()) }
                outlineColor?.let { property("--md-$tag-outline-color", it.toString()) }
                selectedContainerColor?.let { property("--md-$tag-selected-container-color", it.toString()) }
                outlineWidth?.let { property("--md-$tag-outline-width", it.toString()) }
            }
            .toAttrs {
                value?.let { attr("value", it.toString()) }
                if (disabled) attr("disabled", "")
                if (checked) {
                    attr("checked", "")
                }
                indeterminate?.let { attr("indeterminate", "") }
            },
        content = content
    ).also { jsRequire("@material/web/checkbox/checkbox.js") }
}

@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    value: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    checked: Boolean = false,
    indeterminate: Boolean? = null,
    disabled: Boolean = false,
    shape: CSSLengthOrPercentageNumericValue? = null,
    containerSize: CSSLengthOrPercentageNumericValue? = null,
    outlineColor: CSSColorValue? = null,
    outlineWidth: CSSLengthOrPercentageNumericValue? = null,
    selectedContainerColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    BaseCheckbox(
        tag = "checkbox",
        modifier = modifier,
        value = value,
        onClick = onClick,
        checked = checked,
        indeterminate = indeterminate,
        disabled = disabled,
        containerShape = shape,
        containerSize = containerSize,
        outlineColor = outlineColor,
        outlineWidth = outlineWidth,
        selectedContainerColor = selectedContainerColor,
        content = content
    )
}

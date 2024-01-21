package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementScope
import org.w3c.dom.events.EventTarget
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Composable
fun <TElement : MdElement> MdSelectTagElement(
    tagName: String,
    label: String,
    supportingText: String? = null,
    errorText: String? = null,
    required: Boolean? = null,
    onChange: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier,
    content: (@Composable ElementScope<TElement>.() -> Unit)?
) {
    MdTagElement(
        tagName = tagName,
        applyAttrs = modifier.toAttrs {
            classes("md-select")
            attr("label", label)
            supportingText?.let { attr("supportingText", it) }
            errorText?.let { attr("errorText", it) }
            required?.let { attr("required", "") }
            addEventListener("change") {
                onChange(it)
            }
        },
        content = content
    )
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun FilledSelect(
    label: String,
    supportingText: String? = null,
    errorText: String? = null,
    required: Boolean? = null,
    onChange: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdSelectTagElement(
        tagName = "md-filled-select",
        label = label,
        supportingText = supportingText,
        errorText = errorText,
        required = required,
        onChange = onChange,
        modifier = modifier,
        content = content
    ).also { jsRequire("@material/web/select/filled-select.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun OutlinedSelect(
    label: String,
    supportingText: String? = null,
    errorText: String? = null,
    required: Boolean? = null,
    onChange: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdSelectTagElement(
        tagName = "md-outlined-select",
        label = label,
        supportingText = supportingText,
        errorText = errorText,
        required = required,
        onChange = onChange,
        modifier = modifier,
        content = content
    ).also { jsRequire("@material/web/select/outlined-select.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun SelectOption(
    value: String = "",
    headline: String? = null,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-select-option",
        applyAttrs = modifier.toAttrs {
            attr("value", value)
            headline?.let { attr("headline", it) }
        },
        content = content
    ).also { jsRequire("@material/web/select/select-option.js") }
}

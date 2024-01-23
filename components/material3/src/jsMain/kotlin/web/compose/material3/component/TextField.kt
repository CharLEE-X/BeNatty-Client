package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.Span
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire
import web.compose.material3.common.slot

@Composable
fun MdTextFieldTagElement(
    tagName: String,
    value: String?,
    onInput: (String) -> Unit,
    label: String?,
    placeholder: String?,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    containerShape: String?,
    outlineColor: Color.Rgb?,
    inputTextColor: Color.Rgb? = null,
    hasLeadingIcon: Boolean,
    hasTrailingIcon: Boolean,
    type: TextFieldType,
    required: Boolean,
    disabled: Boolean,
    error: Boolean,
    errorText: String?,
    prefixText: String?,
    suffixText: String?,
    supportingText: String?,
    textDirection: String?,
    rows: Int?,
    cols: Int?,
    max: String?,
    maxLength: Int?,
    min: String?,
    minLength: Int?,
    pattern: String?,
    readOnly: Boolean,
    multiple: Boolean,
    step: String?,
    autocomplete: String?,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    MdTagElement(
        tagName = tagName,
        applyAttrs = modifier
            .styleModifier {
                containerShape?.let { property("--md-outlined-text-field-container-shape", it) }
                outlineColor?.let { property("--md-outlined-text-field-outline-color", it.toString()) }
                inputTextColor?.let { property("--md-outlined-text-field-input-text-color", it.toString()) }
            }
            .toAttrs {
                classes("md-text-field")

                value?.let { attr("value", it) }
                attr("type", type.value)
                label?.let { attr("label", it) }
                placeholder?.let { attr("placeholder", it) }
                if (hasLeadingIcon) attr("hasLeadingIcon", "")
                if (hasTrailingIcon) attr("hasTrailingIcon", "")
                if (required) attr("required", "")
                if (error) attr("error", "")
                errorText?.let { attr("errorText", it) }
                prefixText?.let { attr("prefixText", it) }
                suffixText?.let { attr("suffixText", it) }
                if (disabled) attr("disabled", "")
                supportingText?.let { attr("supportingText", it) }
                textDirection?.let { attr("textDirection", it) }
                rows?.let { attr("rows", it.toString()) }
                cols?.let { attr("cols", it.toString()) }
                max?.let { attr("max", it) }
                maxLength?.let { attr("maxLength", it.toString()) }
                min?.let { attr("min", it) }
                minLength?.let { attr("minLength", it.toString()) }
                pattern?.let { attr("pattern", it) }
                if (readOnly) attr("readOnly", "")
                if (multiple) attr("multiple", "")
                step?.let { attr("step", it) }
                autocomplete?.let { attr("autocomplete", it) }

                addEventListener("input") {
                    onInput(it.nativeEvent.target.asDynamic().value.unsafeCast<String>())
                }
            },
    ) {
        com.varabyte.kobweb.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                Span({ slot = "leading-icon" }) { it() }
            }

        inputTextColor?.let {
            Span(
                Modifier.styleModifier {
                    property("--md-outlined-text-field-input-text-color", it.toString())
                }.toAttrs()
            ) { content() }
        } ?: content()

        trailingIcon?.let {
            Span({ slot = "trailing-icon" }) { it() }
        }
        }
    }
}

enum class TextFieldType(val value: String) {
    EMAIL("email"),
    NUMBER("number"),
    PASSWORD("password"),
    SEARCH("search"),
    TEXT("text"),
    URL("url")
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun FilledTextField(
    value: String? = null,
    onInput: (String) -> Unit = {},
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    containerShape: String? = null,
    outlineColor: Color.Rgb? = null,
    inputTextColor: Color.Rgb? = null,
    type: TextFieldType = TextFieldType.TEXT,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false,
    supportingText: String? = null,
    textDirection: String? = null,
    rows: Int? = null,
    cols: Int? = null,
    max: String? = null,
    maxLength: Int? = null,
    min: String? = null,
    minLength: Int? = null,
    pattern: String? = null,
    readOnly: Boolean = false,
    multiple: Boolean = false,
    step: String? = null,
    autocomplete: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    MdTextFieldTagElement(
        tagName = "md-filled-text-field",
        value = value,
        onInput = onInput,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hasLeadingIcon = hasLeadingIcon,
        hasTrailingIcon = hasTrailingIcon,
        containerShape = containerShape,
        outlineColor = outlineColor,
        inputTextColor = inputTextColor,
        type = type,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        prefixText = prefixText,
        suffixText = suffixText,
        supportingText = supportingText,
        textDirection = textDirection,
        rows = rows,
        cols = cols,
        max = max,
        maxLength = maxLength,
        min = min,
        minLength = minLength,
        pattern = pattern,
        readOnly = readOnly,
        multiple = multiple,
        step = step,
        autocomplete = autocomplete,
        modifier = modifier,
        content = content
    ).also { jsRequire("@material/web/textfield/filled-text-field.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun OutlinedTextField(
    value: String? = null,
    onInput: (String) -> Unit = {},
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false,
    containerShape: String? = null,
    outlineColor: Color.Rgb? = null,
    inputTextColor: Color.Rgb? = null,
    type: TextFieldType = TextFieldType.TEXT,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    supportingText: String? = null,
    textDirection: String? = null,
    rows: Int? = null,
    cols: Int? = null,
    max: String? = null,
    maxLength: Int? = null,
    min: String? = null,
    minLength: Int? = null,
    pattern: String? = null,
    readOnly: Boolean = false,
    multiple: Boolean = false,
    step: String? = null,
    autocomplete: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    MdTextFieldTagElement(
        tagName = "md-outlined-text-field",
        value = value,
        onInput = onInput,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        hasLeadingIcon = hasLeadingIcon,
        hasTrailingIcon = hasTrailingIcon,
        containerShape = containerShape,
        outlineColor = outlineColor,
        inputTextColor = inputTextColor,
        type = type,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        prefixText = prefixText,
        suffixText = suffixText,
        supportingText = supportingText,
        textDirection = textDirection,
        rows = rows,
        cols = cols,
        max = max,
        maxLength = maxLength,
        min = min,
        minLength = minLength,
        pattern = pattern,
        readOnly = readOnly,
        multiple = multiple,
        step = step,
        autocomplete = autocomplete,
        modifier = modifier,
        content = content
    ).also { jsRequire("@material/web/textfield/outlined-text-field.js") }
}

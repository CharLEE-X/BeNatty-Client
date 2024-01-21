package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.toAttrs
import org.w3c.dom.HTMLInputElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Composable
fun MdTextFieldTagElement(
    tagName: String,
    value: String = "",
    onInput: (String) -> Unit = {},
    label: String = "",
    type: TextFieldType = TextFieldType.TEXT,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String = "",
    modifier: Modifier,
) {
    MdTagElement(
        tagName = tagName,
        applyAttrs = modifier
            .disabled(disabled)
            .toAttrs {
                classes("md-text-field")
                attr("type", type.value)
                if (required) attr("required", "")
                if (error) attr("error", "")
                attr("errorText", errorText)
                attr("label", label)
                attr("value", value)
                addEventListener("input") {
                    onInput((it.target as? HTMLInputElement)?.value ?: "")
                }
            },
        content = null
    )
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
    value: String = "",
    onInput: (String) -> Unit = {},
    label: String = "",
    type: TextFieldType = TextFieldType.TEXT,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String = "",
    modifier: Modifier = Modifier,
) {
    MdTextFieldTagElement(
        tagName = "md-filled-text-field",
        value = value,
        onInput = onInput,
        label = label,
        type = type,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        modifier = modifier,
    ).also { jsRequire("@material/web/textfield/filled-text-field.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun OutlinedTextField(
    value: String = "",
    onInput: (String) -> Unit = {},
    label: String = "",
    type: TextFieldType = TextFieldType.TEXT,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String = "",
    modifier: Modifier = Modifier,
) {
    MdTextFieldTagElement(
        tagName = "md-outlined-text-field",
        value = value,
        onInput = onInput,
        label = label,
        type = type,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        modifier = modifier,
    ).also { jsRequire("@material/web/textfield/outlined-text-field.js") }
}

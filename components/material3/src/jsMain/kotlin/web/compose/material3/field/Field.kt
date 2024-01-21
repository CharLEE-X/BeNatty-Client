package web.compose.material3.field

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ElementScope
import web.compose.material3.MdElement
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun <TElement : MdElement> MdFieldTagElement(
    name: String,
    label: String? = null,
    value: String? = null,
    errorText: String? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    content: (@Composable ElementScope<TElement>.() -> Unit)?
) {
    MdTagElement(
        tagName = "md-$name-field",
        applyAttrs = modifier.toAttrs {
            classes("md-field")
            if (label != null) attr("label", label)
            if (value != null) attr("value", value)
            if (errorText != null) attr("errorText", errorText)
            if (isError) attr("error", "")
        },
        content = content
    ).also { jsRequire("@material/web/field/$name-field.js") }
}

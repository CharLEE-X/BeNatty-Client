package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import org.w3c.dom.events.EventTarget
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Radio(
    value: String? = null,
    checked: Boolean = false,
    name: String? = null,
    disabled: Boolean = false,
    onChange: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-radio",
    applyAttrs = modifier.toAttrs {
        value?.let { attr("value", it) }
        if (checked) attr("checked", "")
        name?.let { attr("name", it) }
        if (disabled) attr("disabled", "")
        addEventListener("change") {
            onChange(it)
        }
    },
    content = content
).also { jsRequire("@material/web/radio/radio.js") }

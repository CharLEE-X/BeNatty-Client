package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    value: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    checked: Boolean = false,
    indeterminate: Boolean? = null,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-checkbox",
    applyAttrs = modifier
        .onClick { onClick(it) }
        .toAttrs {
            value?.let { attr("value", it.toString()) }
            if (checked) { attr("checked", "") }
            indeterminate?.let { attr("indeterminate", "") }
        },
    content = content
).also { jsRequire("@material/web/checkbox/checkbox.js") }

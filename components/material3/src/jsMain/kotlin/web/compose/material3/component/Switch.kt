package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Switch(
    value: Boolean? = null,
    selected: Boolean = false,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    icons: Boolean = false,
    showOnlySelectedIcon: Boolean = false,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-switch",
    applyAttrs = modifier
        .onClick {
            println("Switch.onClick: $it")
            onClick(it)
        }
        .toAttrs {
            value?.let { attr("value", it.toString()) }
            if (selected) attr("selected", "")
            if (icons) attr("icons", "")
            if (showOnlySelectedIcon) attr("showOnlySelectedIcon", "")
            if (disabled) attr("disabled", "")
        },
    content = null
).also { jsRequire("@material/web/switch/switch.js") }

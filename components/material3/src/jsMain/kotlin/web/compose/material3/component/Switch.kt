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
    selected: Boolean = false,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    icons: Boolean = false,
    showOnlySelectedIcon: Boolean = false,
    disabled: Boolean = false,
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-switch",
    applyAttrs = modifier
        .onClick(onClick)
        .toAttrs {
            if (selected) attr("selected", "")
            if (icons) attr("icons", "")
            if (showOnlySelectedIcon) attr("showOnlySelectedIcon", "")
            if (disabled) attr("disabled", "")
        },
    content = null
).also { jsRequire("@material/web/switch/switch.js") }

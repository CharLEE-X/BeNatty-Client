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
fun Tabs(
    activeTabIndex: Int,
    selectOnFocus: Boolean = true,
    onChange: (SyntheticEvent<EventTarget>) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-tabs",
    applyAttrs = modifier.toAttrs {
        attr("activeTabIndex", activeTabIndex.toString())
        if (selectOnFocus) attr("selectOnFocus", "")
        addEventListener("change") {
            onChange(it)
        }
    },
    content = content
).also { jsRequire("@material/web/tabs/tabs.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun PrimaryTab(
    disabled: Boolean = false,
    active: Boolean = false,
    focusable: Boolean = true,
    inlineIcon: Boolean = false,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-primary-tab",
        applyAttrs = modifier.toAttrs {
            if (disabled) attr("disabled", "")
            if (active) attr("active", "")
            if (focusable) attr("focusable", "")
            if (inlineIcon) attr("inlineIcon", "")
        },
        content = content
    ).also { jsRequire("@material/web/tabs/primary-tab.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun SecondaryTab(
    disabled: Boolean = false,
    active: Boolean = false,
    focusable: Boolean = true,
    inlineIcon: Boolean = false,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-secondary-tab",
        applyAttrs = modifier.toAttrs {
            if (disabled) attr("disabled", "")
            if (active) attr("active", "")
            if (focusable) attr("focusable", "")
            if (inlineIcon) attr("inlineIcon", "")
        },
        content = content
    ).also { jsRequire("@material/web/tabs/secondary-tab.js") }
}


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
fun NavigationTab(
    label: String,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-navigation-tab",
    applyAttrs = modifier
        .onClick { onClick(it) }
        .toAttrs {
            attr("label", label)
        },
    content = content
).also { jsRequire("@material/web/labs/navigationtab/navigation-tab.js") }

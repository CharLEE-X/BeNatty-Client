package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun ModalNavigationDrawer(
    opened: Boolean = false,
    modifier: Modifier = Modifier,
    content: ContentBuilder<NavigationDrawerElement>? = null
) = MdTagElement(
    tagName = "md-navigation-drawer-modal",
    applyAttrs = modifier.toAttrs {
        if (opened) attr("opened", "")
    },
    content = content
).also { jsRequire("@material/web/labs/navigationdrawer/navigation-drawer-modal.js") }

abstract class NavigationDrawerElement : MdElement()

@Suppress("UnsafeCastFromDynamic")
@Composable
fun NavigationDrawer(
    opened: Boolean = false,
    pivot: Pivot? = null,
    modifier: Modifier = Modifier,
    content: ContentBuilder<NavigationDrawerElement>? = null
) = MdTagElement(
    tagName = "md-navigation-drawer",
    applyAttrs = modifier.toAttrs {
        if (opened) {
            attr("opened", "")
        }
        pivot?.let { attr("pivot", it.value) }
    },
    content = content,
).also { jsRequire("@material/web/labs/navigationdrawer/navigation-drawer.js") }


enum class Pivot(val value: String) {
    START("start"),
    END("end"),
}

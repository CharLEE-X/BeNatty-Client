package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.events.EventTarget
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire
import web.compose.material3.common.slot

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    open: Boolean = true,
    onOpening: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onOpened: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosing: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosed: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onCancel: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    headline: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    MdTagElement(
        tagName = "md-dialog",
        applyAttrs = modifier.toAttrs {
            if (open) attr("open", "")
            addEventListener("opening") { onOpening(it) }
            addEventListener("opened") {
                onOpened(it)
                document.body?.style?.apply {
                    overflowY = Overflow.Hidden.toString()
                }
            }
            addEventListener("closing") { onClosing(it) }
            addEventListener("closed") {
                onClosed(it)
                document.body?.style?.apply {
                    overflowY = Overflow.Auto.toString()
                }
            }
            addEventListener("cancel") { onCancel(it) }
        },
    ) {
        headline?.let { Span({ slot = "headline" }) { it() } }
        actions?.let { Span({ slot = "actions" }) { it() } }
        Span({ slot = "content" }) { content() }
    }.also { jsRequire("@material/web/dialog/dialog.js") }
}

package web.compose.material3.dialog

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.events.EventTarget
import web.compose.material3.MdElement
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire
import web.compose.material3.slot

abstract class DialogElement : MdElement()

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    open: Boolean = true,
    fullscreen: Boolean = false,
    onOpening: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onOpened: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosing: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosed: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onCancel: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    headline: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) = MdTagElement(
    tagName = "md-dialog",
    applyAttrs = modifier.toAttrs {
        if (open) attr("open", "")
        if (fullscreen) attr("fullscreen", "")
        addEventListener("opening") { onOpening(it) }
        addEventListener("opened") { onOpened(it) }
        addEventListener("closing") { onClosing(it) }
        addEventListener("closed") { onClosed(it) }
        addEventListener("cancel") { onCancel(it) }
    },
) {
    headline?.let { Span({ slot = "headline" }) { it() } }
    Span({ slot = "content" }) { content() }
    actions?.let { Span({ slot = "actions" }) { it() } }
}.also { jsRequire("@material/web/dialog/dialog.js") }

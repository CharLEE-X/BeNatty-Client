package web.compose.material3.dialog

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.events.EventTarget
import web.compose.material3.MdElement
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire
import web.compose.material3.slot

abstract class DialogElement : MdElement()

/**
 * Dialogs can require an action, communicate information, or help
 * users accomplish a task. There are two types of dialogs: basic and
 * full-screen.
 *
 * #### Description
 * A dialog is a modal window that appears in front of app content to provide
 * critical information or ask for a decision. Dialogs disable all app
 * functionality when they appear, and remain on screen until confirmed,
 * dismissed, or a required action has been taken.
 *
 * Dialogs are purposefully interruptive, so they should be used sparingly.
 * A less disruptive alternative is to use a menu, which provides options
 * without interrupting a user’s experience.
 *
 * On mobile devices only, complex dialogs should be displayed fullscreen.
 *
 * __Example usages:__
 * - Common use cases for basic dialogs include alerts, quick selection, and
 * confirmation.
 * - More complex dialogs may contain actions that require a series of tasks
 * to complete. One example is creating a calendar entry with the event title,
 * date, location, and time.
 */
@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    open: Boolean = true,
    fullscreen: Boolean = false,
    onOpening: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onOpened: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosing: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    onClosed: ((SyntheticEvent<EventTarget>) -> Unit) = {},
    content: ContentBuilder<DialogElement>
) = MdTagElement(
    tagName = "md-dialog",
    applyAttrs = modifier.toAttrs {
        if (open) open()
        if (fullscreen) fullscreen()
        this.onOpening(onOpening)
        this.onOpened(onOpened)
        this.onClosing(onClosing)
        this.onClosed(onClosed)
    },
    content = content
).also {
    webComponentLoader
}

private val webComponentLoader = jsRequire("@material/web/dialog/dialog.js")

private fun AttrsScope<DialogElement>.open(value: Boolean = true) {
    if (value) attr("open", "")
}

private fun AttrsScope<DialogElement>.fullscreen(value: Boolean = true) {
    if (value) attr("fullscreen", "")
}

private fun AttrsScope<DialogElement>.onOpening(handler: (SyntheticEvent<EventTarget>) -> Unit) {
    addEventListener("opening") {
        handler(it)
    }
}

fun AttrsScope<DialogElement>.onOpened(handler: (SyntheticEvent<EventTarget>) -> Unit) {
    addEventListener("opened") {
        handler(it)
    }
}

fun AttrsScope<DialogElement>.onClosing(handler: (SyntheticEvent<EventTarget>) -> Unit) {
    addEventListener("closing") {
        handler(it)
    }
}

private fun AttrsScope<DialogElement>.onClosed(handler: (SyntheticEvent<EventTarget>) -> Unit) {
    addEventListener("closed") {
        handler(it)
    }
}

@Composable
private fun ContentBuilder<DialogElement>.headline(content: @Composable () -> Unit) {
    Span({ slot = "headline" }) {
        content()
    }
}

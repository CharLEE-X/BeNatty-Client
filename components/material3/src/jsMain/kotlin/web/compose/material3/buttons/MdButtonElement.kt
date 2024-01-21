package web.compose.material3.buttons

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ElementScope
import web.compose.material3.MdElement
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

abstract class MdButtonElement : MdElement()

@Suppress("UnsafeCastFromDynamic")
@Composable
fun <TElement : MdButtonElement> MdButtonTagElement(
    name: String,
    onClick: (SyntheticMouseEvent) -> Unit,
    modifier: Modifier,
    content: (@Composable ElementScope<TElement>.() -> Unit)?
) {
    MdTagElement(
        tagName = "md-$name-button",
        applyAttrs = modifier
            .onClick { evt ->
                onClick(evt)
                evt.stopPropagation()
            }
            .toAttrs {
                classes("md-button")
            },
        content = content
    ).also { jsRequire("@material/web/button/$name-button.js") }
}

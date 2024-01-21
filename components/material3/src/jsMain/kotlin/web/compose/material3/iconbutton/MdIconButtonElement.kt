package web.compose.material3.iconbutton

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ElementScope
import web.compose.material3.MdElement
import web.compose.material3.MdTagElement

abstract class MdIconButtonElement : MdElement()

@Composable
fun <TElement : MdIconButtonElement> MdIconButtonTagElement(
    tagName: String,
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier,
    content: (@Composable ElementScope<TElement>.() -> Unit)?
) = MdTagElement(
    tagName = tagName,
    applyAttrs = modifier
        .onClick { onClick(it) }
        .toAttrs {
            classes("md-icon-button")
            toggle?.let { attr("toggle", "") }
            selected?.let { attr("selected", "") }
            disabled?.let { attr("disabled", "") }
        },
    content = content
)

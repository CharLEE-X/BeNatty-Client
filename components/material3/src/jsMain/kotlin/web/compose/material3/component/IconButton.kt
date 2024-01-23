package web.compose.material3.component

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementScope
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Composable
private fun <TElement : MdElement> MdIconButtonTagElement(
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

@Suppress("UnsafeCastFromDynamic")
@Composable
fun IconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdIconButtonTagElement(
    tagName = "md-icon-button",
    toggle = toggle,
    selected = selected,
    disabled = disabled,
    onClick = onClick,
    modifier = modifier,
    content = content
).also { jsRequire("@material/web/iconbutton/icon-button.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun FilledIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdIconButtonTagElement(
    tagName = "md-filled-icon-button",
    toggle = toggle,
    selected = selected,
    disabled = disabled,
    onClick = onClick,
    modifier = modifier,
    content = content
).also { jsRequire("@material/web/iconbutton/filled-icon-button.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun OutlinedIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdIconButtonTagElement(
    tagName = "md-outlined-icon-button",
    toggle = toggle,
    selected = selected,
    disabled = disabled,
    onClick = onClick,
    modifier = modifier,
    content = content
).also { jsRequire("@material/web/iconbutton/outlined-icon-button.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun TonalIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdIconButtonTagElement(
    tagName = "md-filled-tonal-icon-button",
    toggle = toggle,
    selected = selected,
    disabled = disabled,
    onClick = onClick,
    modifier = modifier,
    content = content
).also { jsRequire("@material/web/iconbutton/filled-tonal-icon-button.js") }

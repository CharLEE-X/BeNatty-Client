package web.compose.material3.iconbutton

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun TonalIconButton(
    toggle: Boolean? = null,
    selected: Boolean? = null,
    disabled: Boolean? = null,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdIconButtonElement>? = null
) = MdIconButtonTagElement(
    tagName = "md-filled-tonal-icon-button",
    toggle = toggle,
    selected = selected,
    disabled = disabled,
    onClick = onClick,
    modifier = modifier,
    content = content
).also { jsRequire("@material/web/iconbutton/filled-tonal-icon-button.js") }

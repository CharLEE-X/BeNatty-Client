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
fun Fab(
    label: String? = null,
    fabSize: FabSize = FabSize.MEDIUM,
    onClick: (SyntheticMouseEvent) -> Unit = {},
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-fab",
    applyAttrs = modifier
        .onClick { onClick(it) }
        .toAttrs {
            label?.let { attr("label", it) }
            attr("size", fabSize.value)
        },
    content = content
).also { jsRequire("@material/web/fab/fab.js") }

enum class FabSize(val value: String) {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large")
}

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
fun List(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-list",
        applyAttrs = modifier.toAttrs(),
        content = content
    ).also { jsRequire("@material/web/list/list.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun ListItem(
    headline: String? = null,
    supportingText: String? = null,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdTagElement(
        tagName = "md-list-item",
        applyAttrs = modifier.toAttrs {
            headline?.let { attr("headline", it) }
            supportingText?.let { attr("supportingText", it) }
        },
        content = content
    ).also { jsRequire("@material/web/list/list-item.js") }
}

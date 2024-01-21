package web.compose.material3.list

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.MdInputElement
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun List(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdInputElement>? = null
) = MdTagElement(
    tagName = "md-list",
    applyAttrs = modifier.toAttrs(),
    content = content
).also { jsRequire("@material/web/list/list.js") }

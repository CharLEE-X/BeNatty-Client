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
fun NavigationBar(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-navigation-bar",
    applyAttrs = modifier.toAttrs(),
    content = content
).also { jsRequire("@material/web/labs/navigationbar/navigation-bar.js") }

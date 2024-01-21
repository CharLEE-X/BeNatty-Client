package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.Text
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Icon(
    iconIdentifier: String,
    modifier: Modifier = Modifier,
) {
    MdTagElement(
        tagName = "md-icon",
        applyAttrs = modifier.toAttrs(),
        content = { Text(iconIdentifier) }
    ).also { jsRequire("@material/web/icon/icon.js") }
}

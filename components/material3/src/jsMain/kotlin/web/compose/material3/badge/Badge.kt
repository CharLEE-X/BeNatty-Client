package web.compose.material3.badge

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Badge(
    value: String = "",
    modifier: Modifier = Modifier
) = MdTagElement(
    tagName = "md-badge",
    applyAttrs = modifier.toAttrs {
        attr("value", value)
    },
    content = {}
).also { jsRequire("@material/web/labs/badge/badge.js") }

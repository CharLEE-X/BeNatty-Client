package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Divider(
    inset: Boolean = false,
    insetStart: Boolean = false,
    insetEnd: Boolean = false,
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-divider",
    applyAttrs = modifier.toAttrs {
        if (inset) attr("inset", "")
        if (insetStart) attr("inset-start", "")
        if (insetEnd) attr("inset-end", "")
    },
    content = {}
).also { jsRequire("@material/web/divider/divider.js") }

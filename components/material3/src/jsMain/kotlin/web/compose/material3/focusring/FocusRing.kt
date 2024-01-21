package web.compose.material3.focusring

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun FocusRing(
    visible: Boolean? = null,
    inward: Boolean = false,
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-focus-ring",
    applyAttrs = modifier.toAttrs {
        if (visible != null) attr("visible", "")
        if (inward) attr("inward", "")
    },
    content = null
).also { jsRequire("@material/web/focus/md-focus-ring.js") }

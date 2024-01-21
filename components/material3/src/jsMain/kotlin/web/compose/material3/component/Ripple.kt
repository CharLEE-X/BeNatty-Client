package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Ripple(
    disabled: Boolean? = null,
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-ripple",
    applyAttrs = modifier.toAttrs {
        disabled?.let { attr("disabled", "") }
    },
    content = null
).also { jsRequire("@material/web/ripple/ripple.js") }

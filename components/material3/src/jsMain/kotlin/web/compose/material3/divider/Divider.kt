package web.compose.material3.divider

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

/**
 * A divider is a thin line that groups content in lists and
 * containers.
 *
 * #### Description
 * Dividers can reinforce tapability, such as when used to separate
 * list items or define tappable regions in an accordion.
 */
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

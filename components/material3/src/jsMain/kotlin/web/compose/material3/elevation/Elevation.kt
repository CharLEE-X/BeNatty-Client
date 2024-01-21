package web.compose.material3.elevation

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.CSSStyleVariable
import org.jetbrains.compose.web.css.StylePropertyNumber
import web.compose.material3.MdTagElement
import web.compose.material3.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Elevation(
    level: Int
) = MdTagElement(
    tagName = "md-elevation",
    applyAttrs = Modifier
        .styleModifier { mdElevationLevel(level) }
        .toAttrs(),
    content = null
).also { jsRequire("@material/web/elevation/elevation.js") }

private val mdElevationLevel = CSSStyleVariable<StylePropertyNumber>("--md-elevation-level")

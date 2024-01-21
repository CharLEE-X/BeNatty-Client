package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ElementScope
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Composable
fun <TElement : MdElement> MdProgressTagElement(
    tagName: String,
    value: Float? = null,
    buffer: Float? = null,
    intermediate: Boolean = false,
    fourColor: Boolean = false,
    modifier: Modifier = Modifier,
    content: (@Composable ElementScope<TElement>.() -> Unit)?
) {
    MdTagElement(
        tagName = tagName,
        applyAttrs = modifier.toAttrs {
            classes("md-progress")
            value?.let {
                require(it in 0f..1f)
                attr("value", it.toString())
            }
            buffer?.let {
                require(it in 0f..1f)
                attr("buffer", it.toString())
            }
            if (intermediate) attr("indeterminate", "")
            if (fourColor) attr("four-color", "")
        },
        content = content
    )
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun CircularProgress(
    value: Float? = null,
    buffer: Float? = null,
    intermediate: Boolean = false,
    fourColor: Boolean = false,
    modifier: Modifier = Modifier,
) {
    MdProgressTagElement<MdElement>(
        tagName = "md-circular-progress",
        value = value,
        buffer = buffer,
        intermediate = intermediate,
        fourColor = fourColor,
        modifier = modifier,
        content = {}
    ).also { jsRequire("@material/web/progress/circular-progress.js") }
}

@Suppress("UnsafeCastFromDynamic")
@Composable
fun LinearProgress(
    value: Float? = null,
    buffer: Float? = null,
    intermediate: Boolean = false,
    fourColor: Boolean = false,
    modifier: Modifier = Modifier,
) {
    MdProgressTagElement<MdElement>(
        tagName = "md-linear-progress",
        value = value,
        buffer = buffer,
        intermediate = intermediate,
        fourColor = fourColor,
        modifier = modifier,
        content = {}
    ).also { jsRequire("@material/web/progress/linear-progress.js") }
}

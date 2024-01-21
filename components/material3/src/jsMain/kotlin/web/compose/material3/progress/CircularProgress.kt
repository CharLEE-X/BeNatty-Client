package web.compose.material3.progress

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.jsRequire

/**
 * Circular progress indicators display progress by animating along an
 * invisible circular track in a clockwise direction. They can be applied
 * directly to a surface, such as a button or card.
 *
 * #### Description
 * Progress indicators inform users about the status of ongoing processes.
 * - Determinate indicators display how long a process will take.
 * - Indeterminate indicators express an unspecified amount of wait time.
 */
@Suppress("UnsafeCastFromDynamic")
@Composable
fun CircularProgress(
    value: Float,
    modifier: Modifier = Modifier,
    intermediate: Boolean = false,
    fourColor: Boolean = false,
) {
    MdProgressTagElement(
        tagName = "md-circular-progress",
        applyAttrs = modifier.toAttrs {
            require(value in 0f..1f)
            attr("value", value.toString())
            if (intermediate) attr("indeterminate", "")
            if (fourColor) attr("four-color", "")
        },
        content = {}
    ).also { jsRequire("@material/web/progress/circular-progress.js") }
}

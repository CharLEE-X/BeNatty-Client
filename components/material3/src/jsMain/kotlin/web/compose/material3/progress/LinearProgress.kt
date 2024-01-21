package web.compose.material3.progress

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import web.compose.material3.jsRequire

/**
 * Linear progress indicators display progress by animating along the
 * length of a fixed, visible track.
 *
 * #### Description
 * Progress indicators inform users about the status of ongoing processes.
 * - Determinate indicators display how long a process will take.
 * - Indeterminate indicators express an unspecified amount of wait time.
 */
@Suppress("UnsafeCastFromDynamic")
@Composable
fun LinearProgress(
    value: Float?,
    modifier: Modifier = Modifier,
    intermediate: Boolean = false,
    fourColor: Boolean = false,
) {
    MdProgressTagElement(
        tagName = "md-linear-progress",
        applyAttrs = modifier.toAttrs {
            value?.let {
                require(it in 0f..1f)
                attr("value", value.toString())
            }
            if (intermediate) attr("indeterminate", "")
            if (fourColor) attr("four-color", "")
        },
        content = {}
    ).also { jsRequire("@material/web/progress/linear-progress.js") }
}

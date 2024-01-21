package web.compose.material3.component

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.w3c.dom.HTMLInputElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun Slider(
    value: Float,
    min: Float,
    max: Float,
    step: Float,
    valueLabel: String? = null,
    withTickMarks: Boolean = false,
    withLabel: Boolean = false,
    disabled: Boolean = false,
    onChange: (Float) -> Unit = {},
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-slider",
    applyAttrs = modifier
        .toAttrs {
            attr("value", value.toString())
            attr("min", min.toString())
            attr("max", max.toString())
            attr("step", step.toString())
            valueLabel?.let { attr("valueLabel", valueLabel) }
            if (withTickMarks) attr("withTickMarks", "")
            if (withLabel) attr("withLabel", "")
            if (disabled) attr("disabled", "")
            addEventListener("change") {
                onChange((it.target as? HTMLInputElement)?.value?.toFloat() ?: 0f)
            }
        },
    content = null
).also { jsRequire("@material/web/slider/slider.js") }

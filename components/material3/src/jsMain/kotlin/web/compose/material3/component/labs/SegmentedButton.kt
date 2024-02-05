package web.compose.material3.component.labs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun SegmentedButtonSet(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-outlined-segmented-button-set",
    applyAttrs = modifier.toAttrs {
        classes("card")
    },
    content = content
).also { jsRequire("@material/web/labs/segmentedbuttonset/outlined-segmented-button-set.js") }


@Suppress("UnsafeCastFromDynamic")
@Composable
fun SegmentedButton(
    label: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) = MdTagElement(
    tagName = "md-outlined-segmented-button",
    applyAttrs = modifier
        .onClick { onClick() }
        .toAttrs {
            label?.let { attr("label", it) }
            if (selected) attr("selected", "")
        },
    content = null
).also { jsRequire("@material/web/labs/segmentedbutton/outlined-segmented-button.js") }

package web.compose.material3.component.labs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
fun ElevatedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-elevated-card",
    applyAttrs = modifier
        .styleModifier {
            elevation?.let { property("--md-elevated-card-container-elevation", elevation.toString()) }
        }
        .toAttrs {
            classes("card")
        },
    content = content
).also { jsRequire("@material/web/labs/card/elevated-card.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-outlined-card",
    applyAttrs = modifier
        .styleModifier {
            elevation?.let { property("--md-outlined-card-container-elevation", elevation.toString()) }
        }
        .toAttrs {
        classes("card")
    },
    content = content
).also { jsRequire("@material/web/labs/card/outlined-card.js") }

@Suppress("UnsafeCastFromDynamic")
@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    content: ContentBuilder<MdElement>? = null
) = MdTagElement(
    tagName = "md-filled-card",
    applyAttrs = modifier
        .styleModifier {
            elevation?.let { property("--md-filled-card-container-elevation", elevation.toString()) }
        }
        .toAttrs {
        classes("card")
    },
    content = content
).also { jsRequire("@material/web/labs/card/filled-card.js") }

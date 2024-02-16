package web.compose.material3.component.labs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.common.MdTagElement
import web.compose.material3.common.jsRequire

@Suppress("UnsafeCastFromDynamic")
@Composable
private fun BaseCard(
    tag: String,
    modifier: Modifier,
    elevation: Int?,
    containerShape: CSSLengthOrPercentageNumericValue?,
    shadowColor: CSSColorValue?,
    content: ContentBuilder<MdElement>?
) {
    MdTagElement(
        tagName = "md-$tag",
        applyAttrs = modifier
            .styleModifier {
                elevation?.let { property("--md-$tag-container-elevation", elevation.toString()) }
                containerShape?.let { property("--md-$tag-container-shape", it) }
                shadowColor?.let { property("--md-$tag-container-shadow-color", it.toString()) }
            }
            .toAttrs {
                classes("card")
            },
        content = content
    ).also { jsRequire("@material/web/labs/card/$tag.js") }
}

@Composable
fun ElevatedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    val tag = "elevated-card"
    BaseCard(
        tag = tag,
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape,
        shadowColor = shadowColor,
        content = content
    )
}

@Composable
fun OutlinedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    val tag = "outlined-card"
    BaseCard(
        tag = tag,
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape,
        shadowColor = shadowColor,
        content = content
    )
}

@Composable
fun FilledCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    val tag = "filled-card"
    BaseCard(
        tag = tag,
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape,
        shadowColor = shadowColor,
        content = content
    )
}

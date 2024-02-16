package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.component.labs.ElevatedCard
import web.compose.material3.component.labs.FilledCard
import web.compose.material3.component.labs.OutlinedCard

@Composable
fun AppElevatedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    ElevatedCard(
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape ?: 12.px,
        shadowColor = shadowColor,
        content = content
    )
}

@Composable
fun AppOutlinedCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    OutlinedCard(
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape ?: 12.px,
        shadowColor = shadowColor,
        content = content
    )
}

@Composable
fun AppFilledCard(
    modifier: Modifier = Modifier,
    elevation: Int? = null,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
    shadowColor: CSSColorValue? = null,
    content: ContentBuilder<MdElement>? = null
) {
    FilledCard(
        modifier = modifier,
        elevation = elevation,
        containerShape = containerShape ?: 12.px,
        shadowColor = shadowColor,
        content = content
    )
}

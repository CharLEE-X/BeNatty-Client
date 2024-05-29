package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibility
import com.varabyte.kobweb.silk.components.text.SpanText
import data.GetCatalogPageQuery
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.components.widgets.TextLink
import web.util.onEnterKeyDown

@Composable
fun CatalogItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    currentPrice: String,
    sizes: List<String>,
    media: List<GetCatalogPageQuery.Medium>,
    imageHeight: CSSLengthOrPercentageNumericValue? = 600.px,
) {
    var hovered by remember { mutableStateOf(false) }
    val mainMedia by remember { mutableStateOf(media.firstOrNull()) }
    val previewMedia = media.getOrNull(1) ?: media.firstOrNull()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .whiteSpace(WhiteSpace.NoWrap)
            .textOverflow(TextOverflow.Ellipsis)
            .gap(10.px)
    ) {
        MainImage(
            onClick = onClick,
            mainMedia = mainMedia,
            previewMedia = previewMedia,
            imageHeight = imageHeight,
        )
        SpanText(title)
        SpanText(text = currentPrice)
        Row(
            modifier = Modifier.gap(0.5.em)
        ) {
            sizes.forEach { size ->
                TextLink(
                    text = size,
                    onClick = { }, // TODO: Implement size selection
                )
            }
        }
    }
}

@Composable
private fun MainImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    mainMedia: GetCatalogPageQuery.Medium?,
    previewMedia: GetCatalogPageQuery.Medium?,
    imageHeight: CSSLengthOrPercentageNumericValue?
) {
    var hovered by remember { mutableStateOf(false) }
    val currentMedia = previewMedia?.let {
        if (hovered) previewMedia else (mainMedia ?: previewMedia)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onClick { onClick() }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
            .userSelect(UserSelect.None)
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .thenIf(imageHeight != null) { Modifier.height(imageHeight!!) }
            .objectFit(ObjectFit.Cover)
        currentMedia?.let { media ->
            Image(
                src = media.url,
                alt = media.alt,
                modifier = imageModifier
            )

            var visibilityHovered by remember { mutableStateOf(false) }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(if (visibilityHovered) 0.5.em else 0.25.em)
                    .zIndex(3)
                    .onMouseEnter { visibilityHovered = true }
                    .onMouseLeave { visibilityHovered = false }
                    .thenIf(!(hovered || visibilityHovered)) { Modifier.translateY((50).percent) }
                    .opacity(if (hovered) 1.0 else 0.0)
                    .transition(
                        CSSTransition("translate", 0.3.s, TransitionTimingFunction.EaseInOut),
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease),
                    )
            ) {
                MdiVisibility(style = IconStyle.OUTLINED)
            }
        } ?: Box(imageModifier)
    }
}

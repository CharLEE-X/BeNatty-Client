package web.pages.shop.product.catalogue

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
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibility
import com.varabyte.kobweb.silk.components.text.SpanText
import data.GetCatalogPageQuery
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.pages.shop.home.gridModifier

@Composable
fun CatalogItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    price: Double?,
    media: List<GetCatalogPageQuery.Medium>,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    imageHeight: CSSLengthOrPercentageNumericValue = 600.px
) {
    var hovered by remember { mutableStateOf(false) }
    var currentMedia by remember { mutableStateOf(media.firstOrNull()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
    ) {
        MainImage(
            onClick = onClick,
            media = currentMedia,
            hovered = hovered,
            borderRadius = borderRadius,
            imageHeight = imageHeight
        )
        Box(Modifier.size(0.5.em))
        Miniatures(
            media = media,
            onMiniatureHoveredChanged = { currentMedia = it ?: media.firstOrNull() },
            onMoreClick = { onClick() }
        )
        Box(Modifier.size(0.5.em))
        ItemTitle(
            text = title,
            hovered = hovered,
            onClick = { onClick() },
        )
        ItemText(text = price?.let { "£$it".uppercase() } ?: "To be determined")
    }
}

@Composable
private fun MainImage(
    onClick: () -> Unit,
    media: GetCatalogPageQuery.Medium?,
    hovered: Boolean,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    hoveredScale: Double = 1.02,
    imageHeight: CSSLengthOrPercentageNumericValue
) {
    var thisHovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
            .onClick { onClick() }
            .borderRadius(borderRadius)
            .onMouseEnter { thisHovered = true }
            .onMouseLeave { thisHovered = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .scale(if (hovered) hoveredScale else 1.0)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .height(imageHeight)
            .borderRadius(borderRadius)
            .objectFit(ObjectFit.Cover)
            .thenIf(hovered) { Modifier.scale(hoveredScale) }
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        media?.let { media ->
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
                    .borderRadius(50.percent)
                    .backgroundColor(MaterialTheme.colors.onBackground)
                    .zIndex(3)
                    .onMouseEnter { visibilityHovered = true }
                    .onMouseLeave { visibilityHovered = false }
                    .thenIf(!(thisHovered || visibilityHovered)) { Modifier.translateY((50).percent) }
                    .opacity(if (thisHovered) 1.0 else 0.0)
                    .backdropFilter(blur(12.px))
                    .transition(
                        CSSTransition("translate", 0.3.s, TransitionTimingFunction.EaseInOut),
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease),
                    )
            ) {
                MdiVisibility(
                    style = IconStyle.OUTLINED,
                    modifier = Modifier.color(MaterialTheme.colors.background),
                )
            }
        } ?: Box(
            modifier = imageModifier
        )
    }
}

@Composable
private fun Miniatures(
    media: List<GetCatalogPageQuery.Medium>,
    onMiniatureHoveredChanged: (GetCatalogPageQuery.Medium?) -> Unit,
    totalShow: Int = 4,
    onMoreClick: () -> Unit,
) {
    val miniatures = media.take(totalShow)
    val hasMore = media.size > totalShow
    val emptyOnes: Int = totalShow - miniatures.size

    Row(
        modifier = gridModifier(
            columns = 5,
            rowMinHeight = 100.px,
            gap = 0.px,
        )
    ) {
        miniatures.forEachIndexed { index, media ->
            MiniatureItem(
                media = media,
                onHovered = { hovered ->
                    val item = if (hovered) media else null
                    onMiniatureHoveredChanged(item)
                },
                modifier = Modifier.thenIf(index < (miniatures.size - 1)) { Modifier.padding(right = 0.5.em) }
            )
        }
        repeat(emptyOnes) {
            Box()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (hasMore) {
                SpanText(
                    text = "+${media.size - totalShow}",
                    modifier = Modifier
                        .cursor(Cursor.Pointer)
                        .onClick { onMoreClick() }
                )
            }
        }
    }
}

@Composable
private fun ItemTitle(
    modifier: Modifier = Modifier,
    text: String,
    hovered: Boolean,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
            .cursor(Cursor.Pointer)
    ) {
        ItemText(text = text)
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                .backgroundColor(contentColor)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}

@Composable
private fun ItemText(
    text: String,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface,
) {
    SpanText(
        text = text,
        modifier = Modifier
            .color(contentColor)
            .fontWeight(450)
            .whiteSpace(WhiteSpace.NoWrap)
    )
}

@Composable
private fun MiniatureItem(
    modifier: Modifier = Modifier,
    media: GetCatalogPageQuery.Medium,
    onHovered: (Boolean) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    val outerBorderColor = if (hovered) MaterialTheme.colors.onSurface else Colors.Transparent
    val innerBorderColor = if (hovered) MaterialTheme.colors.background else Colors.Transparent

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .zIndex(1)
            .onMouseOver {
                hovered = true
                onHovered(true)
            }
            .onMouseLeave {
                hovered = false
                onHovered(false)
            }
            .cursor(Cursor.Pointer)
            .borderRadius(12.px)
            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Image(
            src = media.url,
            alt = media.alt,
            modifier = Modifier
                .fillMaxSize()
                .objectFit(ObjectFit.Cover)
                .borderRadius(12.px)
                .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .borderRadius(12.px)
                .border(
                    width = 2.px,
                    color = outerBorderColor,
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.px)
                .borderRadius(12.px)
                .border(
                    width = 2.px,
                    color = innerBorderColor,
                )
        )
    }
}

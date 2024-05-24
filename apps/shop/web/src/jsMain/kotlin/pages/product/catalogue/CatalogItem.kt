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
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
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
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import core.models.Currency
import data.GetCatalogPageQuery
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.pages.home.gridModifier
import web.pages.product.page.ProductPrice
import web.util.onEnterKeyDown

@Composable
fun CatalogItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    regularPrice: Double,
    salePrice: Double?,
    currency: Currency,
    media: List<GetCatalogPageQuery.Medium>,
    imageHeight: CSSLengthOrPercentageNumericValue? = 600.px,
    miniaturesMinHeight: CSSLengthOrPercentageNumericValue = 100.px,
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
            imageHeight = imageHeight,
            modifier = Modifier
        )
        Box(Modifier.size(0.5.em))
        Miniatures(
            media = media,
            onMiniatureHoveredChanged = { currentMedia = it ?: media.firstOrNull() },
            onClick = onClick,
            minHeight = miniaturesMinHeight
        )
        Box(Modifier.size(0.5.em))
        ItemTitle(
            text = title,
            hovered = hovered,
            onClick = { onClick() },
        )
        ProductPrice(
            regularPrice = regularPrice.toString(),
            salePrice = salePrice?.toString(),
            currency = currency
        )
    }
}

@Composable
private fun MainImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    media: GetCatalogPageQuery.Medium?,
    hovered: Boolean,
    hoveredScale: Double = 1.02,
    imageHeight: CSSLengthOrPercentageNumericValue?
) {
    var thisHovered by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onClick { onClick() }
            .onMouseEnter { thisHovered = true }
            .onMouseLeave { thisHovered = false }
            .onFocusIn { focused = true }
            .onFocusOut { focused = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .scale(if (hovered || focused) hoveredScale else 1.0)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            .tabIndex(0)
            .onEnterKeyDown(onClick)
            .userSelect(UserSelect.None)
            .border(
                width = 1.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .thenIf(imageHeight != null) { Modifier.height(imageHeight!!) }
            .objectFit(ObjectFit.Cover)
            .thenIf(hovered || focused) { Modifier.scale(hoveredScale) }
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
                    .backgroundColor(ColorMode.current.opposite.toPalette().background)
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
                    modifier = Modifier.color(ColorMode.current.toPalette().background),
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
    minHeight: CSSLengthOrPercentageNumericValue = 100.px,
    onClick: () -> Unit
) {
    val miniatures = media.take(totalShow)
    val hasMore = media.size > totalShow
    val emptyOnes: Int = totalShow - miniatures.size

    Row(
        modifier = gridModifier(
            columns = 5,
            rowMinHeight = minHeight,
            gap = 6.px,
        )
    ) {
        miniatures.forEachIndexed { index, media ->
            MiniatureItem(
                media = media,
                onHovered = { hovered ->
                    val item = if (hovered) media else null
                    onMiniatureHoveredChanged(item)
                },
                modifier = Modifier
                    .tabIndex(0)
                    .onClick { onClick() }
                    .onEnterKeyDown(onClick)
            )
        }
        repeat(emptyOnes) {
            Box()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .tabIndex(0)
                .onClick { }
        ) {
            if (hasMore) {
                SpanText(
                    text = "+${media.size - totalShow}",
                    modifier = Modifier
                        .cursor(Cursor.Pointer)
                        .onClick { onClick() }
                        .onEnterKeyDown(onClick)
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
    contentColor: CSSColorValue = ColorMode.current.toPalette().color,
    onClick: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
            .cursor(Cursor.Pointer)
            .onFocusIn { focused = true }
            .onFocusOut { focused = false }
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        ItemText(text = text)
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered || focused) 100.percent else 0.percent)
                .backgroundColor(contentColor)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}

@Composable
private fun ItemText(
    text: String,
    contentColor: CSSColorValue = ColorMode.current.toPalette().color,
) {
    SpanText(
        text = text,
        modifier = Modifier
            .color(contentColor)
            .fontWeight(450)
            .whiteSpace(WhiteSpace.NoWrap)
            .textOverflow(TextOverflow.Ellipsis)
            .overflow(Overflow.Hidden)
    )
}

@Composable
private fun MiniatureItem(
    modifier: Modifier = Modifier,
    media: GetCatalogPageQuery.Medium,
    onHovered: (Boolean) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    val outerBorderColor = if (hovered) ColorMode.current.toPalette().color else Colors.Transparent
    val innerBorderColor = if (hovered) ColorMode.current.toPalette().background else Colors.Transparent

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
            .onFocusIn { onHovered(true) }
            .onFocusOut { onHovered(false) }
            .cursor(Cursor.Pointer)
            .userSelect(UserSelect.None)
            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Image(
            src = media.url,
            alt = media.alt,
            modifier = Modifier
                .fillMaxSize()
                .objectFit(ObjectFit.Cover)
                .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.px,
                    color = outerBorderColor,
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.px)
                .border(
                    width = 2.px,
                    color = innerBorderColor,
                )
        )
    }
}

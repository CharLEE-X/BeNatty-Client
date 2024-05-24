package components.widgets

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
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
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
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import core.models.Currency
import data.GetRecommendedProductsQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.util.onEnterKeyDown

@Composable
fun AppMenu(
    modifier: Modifier = Modifier,
    open: Boolean,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    itemsGap: CSSLengthOrPercentageNumericValue = 0.px,
) {
    Box(
        modifier = modifier
            .position(Position.Absolute)
            .zIndex(5)
            .width(200.px)
            .backgroundColor(ColorMode.current.toPalette().background)
            .opacity(if (open) 1.0 else 0.0)
            .visibility(if (open) Visibility.Visible else Visibility.Hidden)
            .userSelect(UserSelect.None)
            .border(
                width = 1.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
            .translate(
                tx = (-12).px,
                ty = if (open) (-5).px else 20.px,
            )
            .transition(
                CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("top", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("visibility", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
            )
            .tabIndex(0)
    ) {
        Column(
            modifier = Modifier
                .gap(itemsGap)
                .fontWeight(400)
                .fontSize(14.px)
                .lineHeight(18.px)
                .padding(leftRight = 20.px, topBottom = 16.px)
        ) {
            items.forEach { item ->
                MenuItem(
                    onStoreMenuItemSelected = onItemSelected,
                    item = item,
                )
            }
        }
    }
}

@Composable
private fun RecommendedProductItem(
    name: String,
    currency: Currency,
    regularPrice: String,
    salePrice: String?,
    media: List<GetRecommendedProductsQuery.Medium>,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .display(DisplayStyle.Flex)
            .gap(0.5.em)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .maxHeight(400.px)
                .fillMaxHeight()
                .aspectRatio(.6f)
                .onMouseOver { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { hovered = true }
                .onFocusOut { hovered = false }
                .cursor(Cursor.Pointer)
                .tabIndex(0)
                .onClick { onClick() }
                .onEnterKeyDown(onClick)
                .overflow(Overflow.Hidden)
                .userSelect(UserSelect.None)
                .draggable(false)
                .transition(
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("transform", 0.2.s, TransitionTimingFunction.Ease)
                )
        ) {
            media.firstOrNull()?.let { firstMedia ->
                val secondMedia = media.getOrNull(1)
                val currentMedia = secondMedia?.let { if (hovered) it else firstMedia } ?: firstMedia

                Image(
                    src = currentMedia.url,
                    alt = firstMedia.alt,
                    modifier = Modifier
                        .fillMaxSize()
                        .objectFit(ObjectFit.Cover)
                )
            }
        }
        SpanText(
            text = name,
            modifier = Modifier
        )
//        ProductPrice(
//            regularPrice = regularPrice,
//            salePrice = salePrice,
//            currency = currency,
//        )
    }
}

@Composable
private fun MenuItem(
    onStoreMenuItemSelected: (String) -> Unit,
    item: String,
    modifier: Modifier = Modifier,
) {
    var itemHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onStoreMenuItemSelected(item) }
            .cursor(Cursor.Pointer)
            .padding(leftRight = 12.px)
            .onMouseEnter {
                itemHovered = true
            }
            .onMouseLeave { itemHovered = false }
            .tabIndex(0)
            .onEnterKeyDown { onStoreMenuItemSelected(item) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .gap(8.px)
        ) {
            SpanText(
                text = item,
                modifier = Modifier
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .onClick { onStoreMenuItemSelected(item) }
                    .height(2.px)
                    .fillMaxWidth(if (itemHovered) 100.percent else 0.percent)
                    .backgroundColor(ColorMode.current.toPalette().color)
                    .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
    }
}

package web.pages.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.rotateY
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSell
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.GetSimilarProductsQuery
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import feature.shop.cart.CartContract
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.H1Variant
import web.HeadlineStyle
import web.components.widgets.Spacer
import web.util.onEnterKeyDown

@Composable
fun SimilarProducts(
    modifier: Modifier,
    vm: ProductPageViewModel,
    productPageState: ProductPageContract.State,
    cartState: CartContract.State,
) {
    if (productPageState.similarProducts.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .margin(top = 2.em)
                .gap(1.em)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.gap(0.5.em)
            ) {
                MdiSell(
                    style = IconStyle.OUTLINED,
                    modifier = Modifier
                        .rotateY(180.deg)
                )
                SpanText(
                    text = getString(Strings.SimilarProducts),
                    modifier = HeadlineStyle.toModifier(H1Variant)
                )
            }
            productPageState.similarProducts.forEach { product ->
                SimilarProductItem(
                    name = product.name,
                    currency = cartState.currency,
                    currentPrice = product.currentPrice,
                    media = product.media.first(),
                    onClick = { vm.trySend(ProductPageContract.Inputs.OnGoToProductClicked(product.id)) }
                )
            }
        }
    }
}

@Composable
private fun SimilarProductItem(
    name: String,
    currency: Currency,
    currentPrice: String,
    media: GetSimilarProductsQuery.Medium,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Flex)
            .gap(1.em)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .maxHeight(200.px)
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
                .scale(if (hovered) 1.02f else 1f)
                .transition(
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("transform", 0.2.s, TransitionTimingFunction.Ease)
                )
        ) {
            Image(
                src = media.url,
                alt = media.alt,
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Cover)
            )
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            SpanText(name)
            Spacer(0.5.em)
            SpanText(currentPrice)
        }
    }
}

@Composable
fun ProductPrice(
    currentPrice: String,
    salePrice: String?,
    currency: Currency,
    containerModifier: Modifier = Modifier,
    initialIsOnSale: Boolean = false,
) {
    var isOnSale by remember { mutableStateOf(initialIsOnSale) }

    LaunchedEffect(Unit) {
        delay(1_000)
        isOnSale = salePrice != null
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = containerModifier
    ) {
        SpanText(
            text = "${currency.symbol}$currentPrice ${currency.code}",
            modifier = Modifier
                .scale(if (isOnSale) 0.8f else 1f)
                .textDecorationLine(if (isOnSale) TextDecorationLine.LineThrough else TextDecorationLine.None)
                .translate(
                    tx = if (isOnSale) 0.25.em else 0.em,
                    ty = if (isOnSale) (-0.5).em else 0.em,
                )
                .transition(
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("tex-decoration", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("translate", 0.3.s, TransitionTimingFunction.cubicBezier(0.4, 0.0, 0.2, 1.0)),
                )
        )
        salePrice?.let { price ->
            SpanText(
                text = " ${currency.symbol}$price ${currency.code}",
                modifier = Modifier
                    .color(if (isOnSale) Colors.Red else Colors.Transparent)
                    .fontWeight(if (isOnSale) FontWeight.Normal else FontWeight.Bold)
                    .translate(
                        tx = if (isOnSale) (-0.5).em else 0.em,
                        ty = if (isOnSale) 0.5.em else 0.em,
                    )
                    .transition(
                        CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("font-weight", 0.3.s, TransitionTimingFunction.Ease),
                    )
            )
        }
    }
}

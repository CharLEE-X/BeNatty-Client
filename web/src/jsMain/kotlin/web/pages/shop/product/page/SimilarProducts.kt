package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
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
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import data.GetSimilarProductsQuery
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.util.cornerRadius
import web.util.onEnterKeyDown

@Composable
fun SimilarProducts(modifier: Modifier, vm: ProductPageViewModel, state: ProductPageContract.State) {
    if (state.similarProducts.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            state.similarProducts.forEach { product ->
                SimilarProductItem(
                    title = product.title,
                    currencySymbol = state.currency.symbol,
                    price = product.price.toString(),
                    salePrice = product.salePrice,
                    media = product.media.first(),
                    onClick = { vm.trySend(ProductPageContract.Inputs.OnGoToProductClicked(product.id)) }
                )
            }
        }
    }
}

@Composable
private fun SimilarProductItem(
    title: String,
    currencySymbol: String,
    price: String,
    salePrice: String?,
    media: GetSimilarProductsQuery.Medium,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .minHeight(100.px)
                .aspectRatio(.6f)
                .onMouseOver { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { hovered = true }
                .onFocusOut { hovered = false }
                .cursor(Cursor.Pointer)
                .borderRadius(cornerRadius)
                .tabIndex(0)
                .onClick { onClick() }
                .onEnterKeyDown(onClick)
                .overflow(Overflow.Hidden)
                .userSelect(UserSelect.None)
                .scale(if (hovered) 1.05f else 1f)
                .draggable(false)
                .transition(CSSTransition("transform", 0.2.s, TransitionTimingFunction.Ease))
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
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            SpanText(
                text = title,
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
            )
            SpanText(
                text = "$currencySymbol$price",
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyMedium)
                    .thenIf(
                        salePrice != null,
                        Modifier
                            .scale(0.8f)
                            .textDecorationLine(TextDecorationLine.LineThrough)
                    )
            )
            salePrice?.let { salePrice ->
                SpanText(
                    text = salePrice,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                )
            }
        }
    }
}

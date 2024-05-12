package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import data.GetCatalogPageQuery
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import feature.shop.cart.CartContract
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.HeadlineStyle
import web.pages.shop.home.gridModifier
import web.pages.shop.product.catalogue.CatalogItem

@Composable
fun YouMayAlsoLike(
    vm: ProductPageViewModel,
    productPageState: ProductPageContract.State,
    cartState: CartContract.State,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .gap(3.em)
            .margin(topBottom = 2.em)
    ) {
        SpanText(
            text = getString(Strings.YouMayAlsoLike).uppercase(),
            modifier = HeadlineStyle.toModifier()
                .fontSize(2.5.cssRem)
        )
        Row(
            modifier = gridModifier(6)
        ) {
            productPageState.recommendedProducts.forEach { product ->
                CatalogItem(
                    title = product.name,
                    regularPrice = product.regularPrice,
                    salePrice = product.salePrice,
                    media = product.media.map {
                        GetCatalogPageQuery.Medium(
                            url = it.url, keyName = it.keyName, alt = it.alt, type = it.type,
                        )
                    },
                    imageHeight = null,
                    miniaturesMinHeight = 80.px,
                    currency = cartState.currency,
                    onClick = { vm.trySend(ProductPageContract.Inputs.OnGoToProductClicked(product.id)) },
                    modifier = Modifier.aspectRatio(0.667)
                )
            }
        }
    }
}

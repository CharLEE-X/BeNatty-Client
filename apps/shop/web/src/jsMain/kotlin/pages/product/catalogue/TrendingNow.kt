package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import data.GetCatalogPageQuery
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.HeadlineStyle
import web.pages.home.gridModifier

@Composable
fun TrendingNow(
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .gap(3.em)
            .margin(topBottom = 2.em)
    ) {
        SpanText(
            text = getString(Strings.TrendingNow).uppercase(),
            modifier = HeadlineStyle.toModifier()
                .fontSize(2.5.cssRem)
                .color(ColorMode.current.toPalette().color)
        )
        Row(
            modifier = gridModifier(6)
        ) {
            state.trendingNowProducts.forEach { product ->
                CatalogItem(
                    title = product.name,
                    regularPrice = product.regularPrice,
                    salePrice = product.salePrice,
                    media = product.media.map {
                        GetCatalogPageQuery.Medium(
                            url = it.url, keyName = it.keyName, alt = it.alt, type = it.type,
                        )
                    },
                    currency = state.currency,
                    imageHeight = null,
                    miniaturesMinHeight = 40.px,
                    onClick = { vm.trySend(CatalogContract.Inputs.OnGoToProductClicked(product.id)) },
                    modifier = Modifier.aspectRatio(0.667)
                )
            }
        }
    }
}

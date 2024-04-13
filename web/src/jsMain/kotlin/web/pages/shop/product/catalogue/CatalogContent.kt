package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import component.localization.Strings
import component.localization.getString
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogVariant
import feature.product.catalog.CatalogViewModel
import feature.product.catalog.CatalogueRoutes
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.Shimmer
import web.components.widgets.ShimmerText
import web.pages.shop.home.gridModifier
import web.util.glossy

@Composable
fun CataloguePage(
    mainRoutes: MainRoutes,
    catalogVariant: CatalogVariant,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        CatalogViewModel(
            scope = scope,
            catalogVariant = catalogVariant,
            catalogueRoutes = CatalogueRoutes(
                onError = { message -> mainRoutes.onError(message) },
                goToProduct = { productId -> mainRoutes.goToProduct(productId) }
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = getString(Strings.ProductPage),
        mainRoutes = mainRoutes,
        spacing = 1.em
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(left = 24.px, right = 24.px, top = 24.px, bottom = 48.px)
                .glossy()
                .gap(2.em)
        ) {
            CatalogBanner(state = state)
            CatalogueHeader(vm, state)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .display(DisplayStyle.Flex)
                    .gap(1.em)
            ) {
                CatalogueFilters(
                    vm = vm,
                    state = state,
                    modifier = Modifier
                        .maxWidth(20.percent)
                        .fillMaxWidth()
                        .position(Position.Relative)
                        .flex("0 0 auto")
                )
                CatalogueContent(
                    vm = vm,
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CatalogueContent(
    modifier: Modifier = Modifier,
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = gridModifier(3)
        ) {
            val imageHeight: CSSLengthOrPercentageNumericValue = 600.px

            if (!state.isLoading) {
                state.products.forEachIndexed { index, product ->
                    CatalogItem(
                        title = product.title,
                        price = product.price,
                        media = product.media,
                        imageHeight = imageHeight,
                        onClick = { vm.trySend(CatalogContract.Inputs.OnGoToProductClicked(product.id)) },
                        modifier = Modifier
                            .thenIf(index > 2) { Modifier.padding(top = 1.em) }
                    )
                }
            } else {
                repeat(3) {
                    ShimmerCatalogItem(imageHeight = imageHeight)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(topBottom = 3.em)
                ) {
                    ShimmerLoader()
                }
            }
        }
    }
}

@Composable
private fun ShimmerCatalogItem(imageHeight: CSSLengthOrPercentageNumericValue) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(0.5.em)
    ) {
        Shimmer(Modifier.fillMaxWidth().height(imageHeight))
        Row(
            modifier = gridModifier(
                columns = 5,
                rowMinHeight = 100.px,
                gap = 0.5.em,
            )
        ) {
            repeat((2..4).random()) {
                Shimmer(Modifier.fillMaxWidth().height(100.px))
            }
        }
        ShimmerText(Modifier.width(100.px))
        ShimmerText(Modifier.width(30.px))
    }
}

@Composable
fun ShimmerLoader() {
    Row(
        modifier = Modifier.gap(0.5.em)
    ) {
        Shimmer(Modifier.size(20.px))
        Shimmer(delay = 100.ms, modifier = Modifier.size(20.px))
        Shimmer(delay = 200.ms, modifier = Modifier.size(20.px))
    }
}

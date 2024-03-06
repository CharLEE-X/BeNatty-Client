package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.thenIf
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import feature.product.catalog.CatalogueRoutes
import feature.product.catalog.Variant
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.pages.shop.home.gridModifier
import web.util.glossy

@Composable
fun CataloguePage(
    mainRoutes: MainRoutes,
    variant: Variant,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        CatalogViewModel(
            scope = scope,
            variant = variant,
            catalogueRoutes = CatalogueRoutes(
                onError = { message -> mainRoutes.onError(message) },
                goToProduct = { productId -> mainRoutes.goToProduct(productId) }
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.productPage,
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
                    .gap(1.em)
            ) {
                CatalogueFilters(
                    vm = vm,
                    state = state,
                    modifier = Modifier
                        .weight(1)
                        .backgroundColor(Colors.LightCoral)
                )
                CatalogueContent(
                    vm = vm,
                    state = state,
                    modifier = Modifier.weight(4)
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
    Row(
        modifier = gridModifier(3).then(modifier)
    ) {
        state.products.forEachIndexed { index, product ->
            CatalogItem(
                title = product.title,
                price = product.price,
                media = product.media,
                onClick = { vm.trySend(CatalogContract.Inputs.OnGoToProductClicked(product.id)) },
                modifier = Modifier.thenIf(index > 2) { Modifier.padding(top = 1.em) }
            )
        }
    }
}
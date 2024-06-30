package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import component.localization.Strings
import component.localization.getString
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogVariant
import feature.product.catalog.CatalogViewModel
import feature.product.catalog.CatalogueRoutes
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.components.layouts.GlobalVMs
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth
import web.components.sections.FreeSection
import web.components.widgets.AppDividerHorizontal
import web.components.widgets.ObserveViewportEntered
import web.components.widgets.Shimmer
import web.components.widgets.ShimmerText
import web.components.widgets.Spacer
import web.pages.home.CategoriesSection
import web.pages.home.CategoryItem
import web.pages.home.gridModifier

@Composable
fun CatalogContent(
    globalVMs: GlobalVMs,
    mainRoutes: MainRoutes,
    desktopNavRoutes: DesktopNavRoutes,
    footerRoutes: FooterRoutes,
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
    var showFilters by remember { mutableStateOf(true) }

    ShopMainLayout(
        title = getString(Strings.ProductPage),
        mainRoutes = mainRoutes,
        spacing = 1.em,
        desktopNavRoutes = desktopNavRoutes,
        footerRoutes = footerRoutes,
        globalVMs = globalVMs,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.px, bottom = 48.px)
        ) {
            AddSection()
            CatalogBanner(state = state)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .maxWidth(oneLayoutMaxWidth)
                    .padding(leftRight = 24.px)
            ) {
                CatalogueHeader(
                    vm = vm,
                    state = state,
                    showFilters = showFilters,
                    onFiltersClicked = { showFilters = !showFilters }
                )
                AppDividerHorizontal(modifier = Modifier.margin(top = 1.em))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .display(DisplayStyle.Flex)
                        .gap(1.em)
                        .margin(top = 2.em)
                ) {
                    if (showFilters) {
                        CatalogueFilters(
                            vm = vm,
                            state = state,
                            modifier = Modifier.width(33.percent)
                        )
                    }
                    CatalogueContent(
                        vm = vm,
                        state = state,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                YouAlsoViewed(vm, state)
                CategoriesSection(
                    isLoading = state.isCatalogConfigLoading,
                    items = state.categorySection.map { CategoryItem(it.id, it.title, it.url) },
                    onItemClick = { vm.trySend(CatalogContract.Inputs.OnCategoryItemClick(it)) }
                )
            }
            WhoWeAre(vm, state)
            Spacer(2.em)
            FreeSection()
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

            if (!state.isCatalogConfigLoading) {
                state.products.forEachIndexed { index, product ->

                    if (index == state.products.size - 4) {
                        ObserveViewportEntered(
                            sectionId = product.id,
                            distanceFromTop = 0.0,
                            onViewportEntered = { vm.trySend(CatalogContract.Inputs.LoadMoreProducts) }
                        )
                    }

                    CatalogItem(
                        title = product.name,
                        currentPrice = product.currentPrice,
                        sizes = product.sizes,
                        media = product.media,
                        imageHeight = imageHeight,
                        onClick = { vm.trySend(CatalogContract.Inputs.OnGoToProductClicked(product.id)) },
                        modifier = Modifier
                            .id(product.id)
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

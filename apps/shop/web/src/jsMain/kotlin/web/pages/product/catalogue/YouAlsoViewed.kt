package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import component.localization.Strings
import component.localization.getString
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import web.components.widgets.HorizontalScrollSection
import web.components.widgets.ScrollableItem

@Composable
fun YouAlsoViewed(vm: CatalogViewModel, state: CatalogContract.State) {
    HorizontalScrollSection(
        title = getString(Strings.YouAlsoViewed).uppercase(),
        seeMoreTitle = getString(Strings.SeeMoreNewArrivals),
        items = state.youAlsoViewed.map { item ->
            ScrollableItem(
                id = item.id,
                urls = item.urls,
                title = item.title,
                price = item.price,
                sizes = item.sizes,
            )
        },
        onItemClicked = { vm.trySend(CatalogContract.Inputs.OnYouAlsoViewedItemClicked(it)) },
        onSeeMoreClicked = { },
    )
}

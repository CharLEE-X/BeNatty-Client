package web.pages.home

import androidx.compose.runtime.Composable
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import web.components.widgets.HorizontalScrollSection
import web.components.widgets.ScrollableItem

@Composable
fun Featured(vm: HomeViewModel, state: HomeContract.State) {
    HorizontalScrollSection(
        title = state.featured.title.uppercase(),
        seeMoreTitle = getString(Strings.SeeMoreFeatured),
        items = state.featured.items.map { item ->
            ScrollableItem(
                id = item.id,
                urls = item.urls,
                title = item.title,
                price = item.price,
                sizes = item.sizes,
            )
        },
        onSeeMoreClicked = { vm.trySend(HomeContract.Inputs.OnSeeMoreFeaturedClicked) },
        onItemClicked = { vm.trySend(HomeContract.Inputs.OnFeaturedClicked(it)) },
    )
}

package web.pages.shop.home

import androidx.compose.runtime.Composable
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import web.components.widgets.HorizontalScrollSection
import web.components.widgets.ScrollableItem

@Composable
fun JustArrived(vm: HomeViewModel, state: HomeContract.State) {
    HorizontalScrollSection(
        title = getString(Strings.JustArrived).uppercase(),
        seeMoreTitle = getString(Strings.SeeMoreNewArrivals),
        items = state.justArrived.map { item ->
            ScrollableItem(
                id = item.id,
                urls = item.urls,
                title = item.title,
                price = item.price,
                sizes = item.sizes,
            )
        },
        onSeeMoreClicked = { vm.trySend(HomeContract.Inputs.OnSeeMoreNewArrivalsClicked) },
        onItemClicked = { vm.trySend(HomeContract.Inputs.OnJustArrivedClicked(it)) },
    )
}

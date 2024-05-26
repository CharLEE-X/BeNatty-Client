package pages.home

import androidx.compose.runtime.Composable
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import web.components.widgets.HorizontalScrollSection
import web.components.widgets.ScrollableItem

@Composable
fun OurFavorites(vm: HomeViewModel, state: HomeContract.State) {
    HorizontalScrollSection(
        title = getString(Strings.OurFavourites).uppercase(),
        seeMoreTitle = getString(Strings.ViewOurFavorites),
        items = state.featured.map { item ->
            ScrollableItem(
                id = item.id,
                urls = item.urls,
                title = item.title,
                price = item.price,
                sizes = item.sizes,
            )
        },
        onItemClicked = { vm.trySend(HomeContract.Inputs.OnFavoriteClicked(it)) },
        onSeeMoreClicked = { vm.trySend(HomeContract.Inputs.OnSeeAllFavoritesClicked) },
    )
}

package web.pages.shop.account.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.shop.account.wishlist.WishlistContract
import feature.shop.account.wishlist.WishlistViewModel
import feature.shop.navbar.DesktopNavContract
import web.components.layouts.AccountLayout
import web.components.widgets.PageHeader

@Composable
fun WishlistPage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        WishlistViewModel(
            scope = scope,
            onError = onError,
        )
    }

    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = DesktopNavContract.AccountMenuItem.WISHLIST,
        logoutText = state.strings.logout,
        onLogoutClicked = { vm.trySend(WishlistContract.Inputs.OnLogoutClicked) },
        onMenuItemClicked = onMenuItemClicked,
    ) {
        PageHeader("Wishlist")
    }
}

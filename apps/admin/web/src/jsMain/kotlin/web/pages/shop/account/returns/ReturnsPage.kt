package web.pages.shop.account.returns

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.shop.account.returns.ReturnsContract
import feature.shop.account.returns.ReturnsViewModel
import feature.shop.navbar.NavbarContract
import web.components.layouts.AccountLayout
import web.components.widgets.PageHeader

@Composable
fun ReturnsPage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (NavbarContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ReturnsViewModel(
            scope = scope,
            onError = onError,
        )
    }

    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = NavbarContract.AccountMenuItem.WISHLIST,
        logoutText = state.strings.logout,
        onLogoutClicked = { vm.trySend(ReturnsContract.Inputs.OnLogoutClicked) },
        onMenuItemClicked = onMenuItemClicked,
    ) {
        PageHeader("Returns")
    }
}

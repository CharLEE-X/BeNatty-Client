package web.pages.account.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.shop.account.profile.ProfileContract
import feature.shop.account.profile.ProfileViewModel
import feature.shop.navbar.NavbarContract
import web.components.layouts.AccountLayout
import web.components.widgets.PageHeader

@Composable
fun OrderPage(
    onError: suspend (String) -> Unit,
    onMenuItemClicked: (NavbarContract.AccountMenuItem) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProfileViewModel(
            scope = scope,
            onError = onError,
        )
    }

    @Suppress("UNUSED_VARIABLE")
    val state by vm.observeStates().collectAsState()

    AccountLayout(
        item = NavbarContract.AccountMenuItem.ORDERS,
        logoutText = "Logout",
        onLogoutClicked = { vm.trySend(ProfileContract.Inputs.OnLogoutClicked) },
        onMenuItemClicked = onMenuItemClicked,
    ) {
        PageHeader("Orders")
    }
}

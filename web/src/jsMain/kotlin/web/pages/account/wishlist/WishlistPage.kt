package web.pages.account.wishlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.account.wishlist.WishlistViewModel
import web.components.widgets.PageHeader

@Composable
fun WishlistPage(
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        WishlistViewModel(
            scope = scope,
            onError = onError,
        )
    }

    @Suppress("UNUSED_VARIABLE")
    val state by vm.observeStates().collectAsState()

    PageHeader("Wishlist")
}

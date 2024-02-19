package web.pages.shop.account.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.shop.account.profile.ProfileViewModel
import web.components.widgets.PageHeader

@Composable
fun OrderPage(
    onError: suspend (String) -> Unit,
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

    PageHeader("Orders")
}

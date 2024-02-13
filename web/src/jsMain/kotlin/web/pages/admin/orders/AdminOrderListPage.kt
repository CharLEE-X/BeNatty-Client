package web.pages.admin.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.router.RouterViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.ListPageLayout

@Composable
fun AdminOrderListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.ORDER,
            isSlot1Sortable = true,
            showSlot2 = true,
            isSlot2Sortable = true,
            showSlot3 = true,
            isSlot3Sortable = true,
            showSlot4 = false,
            isSlot4Sortable = false,
            showSlot5 = false,
            isSlot5Sortable = false,
            showSlot6 = false,
            isSlot6Sortable = false,
            scope = scope,
            onError = onError,
            goToCreate = {},
            goToDetail = {}
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = "Admin Order List",
        router = router,
        isLoading = false,
    ) {
        ListPageLayout(state, vm)
    }
}

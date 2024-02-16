package web.pages.admin.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.router.RouterViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout

@Composable
fun AdminOrderListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goToAdminHome: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.ORDER,
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
        showEditedButtons = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        goToAdminHome = goToAdminHome,
    ) {
        OneLayout(
            title = "Orders",
            onGoBack = { router.trySend(RouterContract.Inputs.GoBack()) },
            hasBackButton = false,
            actions = {},
        ) {
            ListPageLayout(state, vm)
        }
    }
}

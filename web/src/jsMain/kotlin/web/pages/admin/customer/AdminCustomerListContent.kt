package web.pages.admin.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.admin.list.adminListStrings
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout
import web.components.widgets.CreateButton

@Composable
fun AdminCustomerListPage(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCustomer: (String) -> Unit,
    goToCreateCustomer: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.Customer,
            scope = scope,
            onError = onError,
            goToDetail = goToCustomer,
            goToCreate = goToCreateCustomer,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = adminListStrings(state.dataType).title,
        isLoading = state.isLoading,
        showEditedButtons = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        adminRoutes = adminRoutes,
    ) {
        OneLayout(
            title = adminListStrings(state.dataType).title,
            subtitle = null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = false,
            actions = { CreateButton { vm.trySend(AdminListContract.Inputs.Click.Create) } },
            content = { ListPageLayout(state, vm) }
        )
    }
}

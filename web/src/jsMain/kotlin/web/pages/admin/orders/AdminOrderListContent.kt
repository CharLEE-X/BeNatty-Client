package web.pages.admin.orders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.admin.list.adminListStrings
import theme.MaterialTheme
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton

@Composable
fun AdminOrderListPage(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCreateOrder: () -> Unit,
    goToOrder: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.ORDER,
            scope = scope,
            onError = onError,
            goToCreate = goToCreateOrder,
            goToDetail = goToOrder,
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
            actions = {
                AppFilledButton(
                    onClick = { vm.trySend(AdminListContract.Inputs.Click.Create) },
                    leadingIcon = { MdiCreate() },
                    containerColor = MaterialTheme.colors.tertiary,
                ) {
                    SpanText(text = getString(Strings.Create))
                }
            },
            content = {
                ListPageLayout(state, vm)
            }
        )
    }
}

package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import web.components.layouts.ListPageLayout

@Composable
fun AdminUserListPage(
    onError: suspend (String) -> Unit,
    onUserClick: (String) -> Unit,
    goToCreate: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.USER,
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
            goToCreate = goToCreate,
            goToDetail = onUserClick
        )
    }
    val state by vm.observeStates().collectAsState()

    ListPageLayout(state, vm)
}

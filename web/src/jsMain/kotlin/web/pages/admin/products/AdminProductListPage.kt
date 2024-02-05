package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import web.components.layouts.ListPageLayout

@Composable
fun AdminProductListPage(
    onError: suspend (String) -> Unit,
    onProductClick: (String) -> Unit,
    goToCreateProduct: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.PRODUCT,
            isSlot1Sortable = true,
            showSlot2 = true,
            isSlot2Sortable = false,
            showSlot3 = true,
            isSlot3Sortable = true,
            showSlot4 = true,
            isSlot4Sortable = true,
            showSlot5 = true,
            isSlot5Sortable = true,
            showSlot6 = true,
            isSlot6Sortable = true,
            scope = scope,
            onError = onError,
            goToCreate = goToCreateProduct,
            goToDetail = onProductClick
        )
    }
    val state by vm.observeStates().collectAsState()

    ListPageLayout(state, vm)
}

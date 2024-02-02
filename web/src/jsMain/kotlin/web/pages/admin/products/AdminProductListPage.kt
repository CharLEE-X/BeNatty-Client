package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.product.list.AdminProductListViewModel
import web.components.layouts.ListItem
import web.components.layouts.ListPageLayout

@Composable
fun AdminProductListPage(
    onError: suspend (String) -> Unit,
    onProductClick: (String) -> Unit,
    goToCreateProduct: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductListViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    ListPageLayout(
        title = state.strings.products,
        createText = state.strings.newProduct,
        pressCreateToStartText = state.strings.pressCreateToStart,
        onAddClick = { goToCreateProduct() },
        onItemClick = { onProductClick(it) },
        items = state.products.map {
            ListItem(
                id = it.id.toString(),
                name = it.name,
            )
        },
    )
}

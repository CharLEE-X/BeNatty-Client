package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.category.list.AdminCategoryListViewModel
import web.components.layouts.ListItem
import web.components.layouts.ListPageLayout

@Composable
fun AdminCategoryListPage(
    onError: suspend (String) -> Unit,
    onItemClick: (String) -> Unit,
    goToCreate: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryListViewModel(
            scope = scope,
            onError = onError,
            goToCreateCategory = goToCreate,
            onCategoryClick = onItemClick
        )
    }
    val state by vm.observeStates().collectAsState()

    ListPageLayout(
        title = state.strings.categories,
        createText = state.strings.newCategory,
        pressCreateToStartText = state.strings.pressCreateToStart,
        onAddClick = goToCreate,
        onItemClick = onItemClick,
        items = state.categories.map {
            ListItem(
                id = it.id.toString(),
                name = it.name,
            )
        },
    )
}

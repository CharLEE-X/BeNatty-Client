package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import feature.admin.tag.list.AdminUserListViewModel
import web.components.layouts.ListItem
import web.components.layouts.ListPageLayout

@Composable
fun AdminUserListPage(
    onError: suspend (String) -> Unit,
    onUserClick: (String) -> Unit,
    goToCreateUser: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminUserListViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    ListPageLayout(
        title = state.strings.users,
        createText = state.strings.newUser,
        pressCreateToStartText = state.strings.pressCreateToStart,
        onAddClick = { goToCreateUser() },
        onItemClick = { onUserClick(it) },
        items = state.users.map {
            ListItem(
                id = it.id.toString(),
                name = it.email,
            )
        },
    )
}

package web.pages.admin.users

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.ListPageLayout

@Composable
fun AdminUserListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
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
            goToDetail = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminUserPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
            goToCreate = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminUserPageExisting.matcher.routeFormat
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = "Admin Users Page",
        router = router,
        isLoading = state.isLoading,
    ) {
        ListPageLayout(state, vm)
    }
}

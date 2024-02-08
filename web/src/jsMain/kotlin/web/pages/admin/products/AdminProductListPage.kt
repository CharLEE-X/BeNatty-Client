package web.pages.admin.products

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
fun AdminProductListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
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
            goToCreate = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.AdminProductPageNew.matcher.routeFormat)
                )
            },
            goToDetail = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminProductPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(state.strings.title, router) {
        ListPageLayout(state, vm)
    }
}

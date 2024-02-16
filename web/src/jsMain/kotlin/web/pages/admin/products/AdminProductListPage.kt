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
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
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
    println("DEBUG product list top ")

    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.PRODUCT,
            scope = scope,
            onError = onError,
            goToCreate = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.AdminProductPageNew.matcher.routeFormat)
                )
            },
            goToDetail = { id ->
                println("DEBUG product list -> goToDetail: $id")
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

    AdminLayout(
        title = state.strings.title,
        router = router,
        isLoading = state.isLoading,
    ) {
        Button(
            onClick = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminProductPageNew.route
                    )
                )
            }
        ) {
            SpanText("goto product")
        }
        ListPageLayout(state, vm)
    }
}

package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.layouts.AdminLayout
import web.components.layouts.ListPageLayout
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton

@Composable
fun AdminTagListPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goToAdminHome: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminListViewModel(
            dataType = AdminListContract.DataType.TAG,
            scope = scope,
            onError = onError,
            goToCreate = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        Screen.AdminTagCreate.matcher.routeFormat
                    )
                )
            },
            goToDetail = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        Screen.AdminTagPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = "Admin Tag List",
        router = router,
        isLoading = false,
        goToAdminHome = goToAdminHome,
    ) {
        OneLayout(
            title = state.strings.title,
            onGoBack = { router.trySend(RouterContract.Inputs.GoBack()) },
            hasBackButton = false,
            actions = {
                AppFilledButton(
                    onClick = { router.trySend(RouterContract.Inputs.GoToDestination(Screen.AdminTagCreate.matcher.routeFormat)) },
                    leadingIcon = { MdiCreate() },
                    containerColor = MaterialTheme.colors.mdSysColorTertiary.value(),
                ) {
                    SpanText(text = state.strings.create)
                }
            },
            content = {
                ListPageLayout(state, vm)
            }
        )
    }
}

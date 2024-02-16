package web.pages.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.dashboard.AdminDashboardViewModel
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedCard
import web.compose.material3.component.Divider

@Composable
fun AdminDashboardPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goToAdminHome: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminDashboardViewModel(
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = state.strings.home,
        router = router,
        isLoading = false,
        showEditedButtons = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        goToAdminHome = goToAdminHome,
    ) {
        OneLayout(
            title = state.strings.home,
            onGoBack = { router.trySend(RouterContract.Inputs.GoBack()) },
            hasBackButton = false,
            actions = {},
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .gap(1.em)
            ) {
                Divider()
                AppOutlinedCard(
                    modifier = Modifier.padding(2.em)
                ) {
                    Column(
                        modifier = Modifier.gap(1.em)
                    ) {
                        SpanText(
                            text = state.strings.stats,
                            modifier = Modifier.roleStyle(MaterialTheme.typography.headlineMedium)
                        )
                        SpanText(
                            text = "${state.strings.users}: ${state.stats.totalUsers}",
                            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                        )
                        SpanText(
                            text = "${state.strings.products}: ${state.stats.totalProducts}",
                            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                        )
                        SpanText(
                            text = "${state.strings.orders}: ${state.stats.totalOrders}",
                            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                        )
                    }
                }
            }
        }
    }
}

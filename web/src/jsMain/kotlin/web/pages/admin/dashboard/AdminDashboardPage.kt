package web.pages.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import web.compose.material3.component.Divider
import web.compose.material3.component.labs.OutlinedCard

@Composable
fun AdminDashboardPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
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
        title = "Admin Dashboard",
        router = router,
        isLoading = false,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .gap(1.em)
        ) {
            SpanText(
                text = state.strings.dashboard,
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineLarge)
            )
            Divider()
            OutlinedCard(
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

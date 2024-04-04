package web.pages.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.dashboard.AdminDashboardViewModel
import feature.admin.dashboard.adminDashboardStrings
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.util.glossy

@Composable
fun AdminDashboardPage(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
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
        title = adminDashboardStrings.home,
        isLoading = false,
        showEditedButtons = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        adminRoutes = adminRoutes,
    ) {
        OneLayout(
            title = adminDashboardStrings.home,
            subtitle = null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = false,
            actions = {},
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .gap(1.em)
            ) {
                Box(
                    modifier = Modifier
                        .glossy()
                        .padding(2.em)
                ) {
                    Column(
                        modifier = Modifier.gap(1.em)
                    ) {
                        SpanText(adminDashboardStrings.stats)
                        SpanText("${adminDashboardStrings.customers}: ${state.stats.totalUsers}")
                        SpanText("${adminDashboardStrings.products}: ${state.stats.totalProducts}")
                        SpanText("${adminDashboardStrings.orders}: ${state.stats.totalOrders}")
                    }
                }
            }
        }
    }
}

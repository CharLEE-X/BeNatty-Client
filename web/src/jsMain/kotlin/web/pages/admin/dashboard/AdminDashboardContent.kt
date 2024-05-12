package web.pages.admin.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings.Categories
import component.localization.Strings.Customers
import component.localization.Strings.Orders
import component.localization.Strings.Products
import component.localization.Strings.Tags
import component.localization.getString
import feature.admin.dashboard.AdminDashboardContract
import feature.admin.dashboard.AdminDashboardViewModel
import feature.admin.dashboard.adminDashboardStrings
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
import web.components.widgets.DeleteButton
import web.components.widgets.SwitchSection

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

    var count by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        while (true) {
            count++
            delay(1000)
        }
    }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                Stats(state)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                DataGenerator(vm, state)
                DataDeleter(vm, state)
            }
        }
    }
}

@Composable
private fun Stats(state: AdminDashboardContract.State) {
    CardSection(title = "Stats") {
        LoaderComponent(state.isStatsLoading) {
            SpanText("${getString(Customers)}: ${state.stats.totalCustomers}")
            SpanText("${getString(Products)}: ${state.stats.totalProducts}")
            SpanText("${getString(Orders)}: ${state.stats.totalOrders}")
            SpanText("${getString(Categories)}: ${state.stats.totalCategories}")
            SpanText("${getString(Tags)}: ${state.stats.totalTags}")
        }
    }
}

@Composable
private fun DataGenerator(vm: AdminDashboardViewModel, state: AdminDashboardContract.State) {
    CardSection(title = "Generator") {
        LoaderComponent(state.isGenerating) {
            AppOutlinedTextField(
                value = state.productsToGenerate.toString(),
                onValueChange = { vm.trySend(AdminDashboardContract.Inputs.SetProductsToGenerate(it)) },
                label = getString(Products),
//                type = TextFieldType.NUMBER,
                modifier = Modifier.fillMaxWidth()
            )
            AppOutlinedTextField(
                value = state.customersToGenerate.toString(),
                onValueChange = { vm.trySend(AdminDashboardContract.Inputs.SetCustomersToGenerate(it)) },
                label = getString(Customers),
//                type = TextFieldType.NUMBER,
                modifier = Modifier.fillMaxWidth()
            )
            AppOutlinedTextField(
                value = state.ordersToGenerate.toString(),
                onValueChange = { vm.trySend(AdminDashboardContract.Inputs.SetOrdersToGenerate(it)) },
                label = getString(Orders),
//                type = TextFieldType.NUMBER,
                modifier = Modifier.fillMaxWidth()
            )
            CreateButton { vm.trySend(AdminDashboardContract.Inputs.OnGenerateClicked) }
        }
    }
}

@Composable
private fun DataDeleter(vm: AdminDashboardViewModel, state: AdminDashboardContract.State) {
    CardSection(title = "Data deleter") {
        LoaderComponent(state.isDeleting) {
            SwitchSection(
                title = getString(Products),
                selected = state.deleteProducts,
                onClick = { vm.trySend(AdminDashboardContract.Inputs.OnDeleteProductsClicked) },
            )
            SwitchSection(
                title = getString(Customers),
                selected = state.deleteCustomers,
                onClick = { vm.trySend(AdminDashboardContract.Inputs.OnDeleteCustomersClicked) },
            )
            SwitchSection(
                title = getString(Orders),
                selected = state.deleteOrders,
                onClick = { vm.trySend(AdminDashboardContract.Inputs.OnDeleteOrdersClicked) },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer()
                DeleteButton { vm.trySend(AdminDashboardContract.Inputs.OnDeleteGeneratedClicked) }
            }
        }
    }
}

@Composable
private fun LoaderComponent(show: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.gap(1.em)
        ) {
            content()
        }
        if (show) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .onClick { it.preventDefault() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .backgroundColor(Colors.LightGray.copy(alpha = 50))
                )
                SpanText("Loading...")
            }
        }
    }
}

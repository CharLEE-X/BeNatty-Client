package feature.admin.dashboard

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AdminService
import data.type.DeleteGenerateDataInput
import data.type.GenerateDataInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State>

internal class AdminDashboardInputHandler :
    KoinComponent,
    InputHandler<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State> {

    private val adminService: AdminService by inject()

    override suspend fun InputHandlerScope<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State>.handleInput(
        input: AdminDashboardContract.Inputs,
    ) = when (input) {
        AdminDashboardContract.Inputs.Init -> handleInit()
        AdminDashboardContract.Inputs.GetStats -> handleGetStats()

        AdminDashboardContract.Inputs.OnGenerateClicked -> handleGenerateClicked()
        AdminDashboardContract.Inputs.OnDeleteGeneratedClicked -> handleDeleteGenerateClicked()

        is AdminDashboardContract.Inputs.SetIsStatsLoading -> updateState { it.copy(isStatsLoading = input.isLoading) }
        is AdminDashboardContract.Inputs.SetIsGenerating -> updateState { it.copy(isGenerating = input.isGenerating) }
        is AdminDashboardContract.Inputs.SetStats -> updateState { it.copy(stats = input.stats) }
        is AdminDashboardContract.Inputs.SetProductsToGenerate -> updateState { it.copy(productsToGenerate = input.products.toInt()) }
        is AdminDashboardContract.Inputs.SetCustomersToGenerate -> updateState { it.copy(customersToGenerate = input.customers.toInt()) }
        is AdminDashboardContract.Inputs.SetOrdersToGenerate -> updateState { it.copy(ordersToGenerate = input.orders.toInt()) }
        AdminDashboardContract.Inputs.OnDeleteCustomersClicked -> updateState { it.copy(deleteCustomers = !it.deleteCustomers) }
        AdminDashboardContract.Inputs.OnDeleteOrdersClicked -> updateState { it.copy(deleteOrders = !it.deleteOrders) }
        AdminDashboardContract.Inputs.OnDeleteProductsClicked -> updateState { it.copy(deleteProducts = !it.deleteProducts) }
        is AdminDashboardContract.Inputs.SetIsDeleting -> updateState { it.copy(isDeleting = input.isDeleting) }
    }

    private suspend fun InputScope.handleDeleteGenerateClicked() {
        val state = getCurrentState()

        if (!state.deleteProducts && !state.deleteCustomers && !state.deleteOrders) {
            postEvent(AdminDashboardContract.Events.OnError("At least one field must be selected"))
            return
        }

        sideJob("handleDeleteGenerateClicked") {
            postInput(AdminDashboardContract.Inputs.SetIsDeleting(true))
            val input = DeleteGenerateDataInput(
                products = state.deleteProducts,
                users = state.deleteCustomers,
                orders = state.deleteOrders,
            )
            adminService.deleteGeneratedData(input).fold(
                { postEvent(AdminDashboardContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminDashboardContract.Inputs.GetStats) },
            )
            postInput(AdminDashboardContract.Inputs.SetIsDeleting(false))
        }
    }

    private suspend fun InputScope.handleGenerateClicked() {
        val state = getCurrentState()

        val productsIsNumber = state.productsToGenerate.toString().toIntOrNull() != null
        val customersIsNumber = state.customersToGenerate.toString().toIntOrNull() != null
        val ordersIsNumber = state.ordersToGenerate.toString().toIntOrNull() != null

        if (!productsIsNumber || !customersIsNumber || !ordersIsNumber) {
            postEvent(AdminDashboardContract.Events.OnError("All fields must be numbers"))
            return
        }

        sideJob("handleGenerateClicked") {
            postInput(AdminDashboardContract.Inputs.SetIsGenerating(true))
            val input = GenerateDataInput(
                products = state.productsToGenerate,
                users = state.customersToGenerate,
                orders = state.ordersToGenerate,
            )
            adminService.generateData(input).fold(
                { postEvent(AdminDashboardContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminDashboardContract.Inputs.GetStats) },
            )
            postInput(AdminDashboardContract.Inputs.SetIsGenerating(false))
        }
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(AdminDashboardContract.Inputs.GetStats)
        }
    }

    private suspend fun InputScope.handleGetStats() {
        sideJob("handleGetStats") {
            postInput(AdminDashboardContract.Inputs.SetIsStatsLoading(true))
            adminService.getStats().fold(
                { postEvent(AdminDashboardContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminDashboardContract.Inputs.SetStats(it.getStats)) },
            )
            postInput(AdminDashboardContract.Inputs.SetIsStatsLoading(false))
        }
    }
}

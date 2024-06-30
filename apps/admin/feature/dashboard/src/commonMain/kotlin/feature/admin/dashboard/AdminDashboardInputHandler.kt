package feature.admin.dashboard

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AdminService
import data.type.DeleteGenerateDataInput
import data.type.GenerateDataInput
import feature.admin.dashboard.AdminDashboardContract.Events
import feature.admin.dashboard.AdminDashboardContract.Inputs
import feature.admin.dashboard.AdminDashboardContract.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<Inputs, Events, State>

internal class AdminDashboardInputHandler : KoinComponent, InputHandler<Inputs, Events, State> {
    private val adminService: AdminService by inject()

    override suspend fun InputHandlerScope<Inputs, Events, State>.handleInput(input: Inputs) =
        when (input) {
            Inputs.Init -> handleInit()
            Inputs.GetStats -> handleGetStats()

            Inputs.OnGenerateClicked -> handleGenerateClicked()
            Inputs.OnDeleteGeneratedClicked -> handleDeleteGenerateClicked()

            is Inputs.SetIsStatsLoading -> updateState { it.copy(isStatsLoading = input.isLoading) }
            is Inputs.SetIsGenerating -> updateState { it.copy(isGenerating = input.isGenerating) }
            is Inputs.SetStats -> updateState { it.copy(stats = input.stats) }
            is Inputs.SetProductsToGenerate -> updateState { it.copy(productsToGenerate = input.products.toInt()) }
            is Inputs.SetCustomersToGenerate -> updateState { it.copy(customersToGenerate = input.customers.toInt()) }
            is Inputs.SetOrdersToGenerate -> updateState { it.copy(ordersToGenerate = input.orders.toInt()) }
            Inputs.OnDeleteCustomersClicked -> updateState { it.copy(deleteCustomers = !it.deleteCustomers) }
            Inputs.OnDeleteOrdersClicked -> updateState { it.copy(deleteOrders = !it.deleteOrders) }
            Inputs.OnDeleteProductsClicked -> updateState { it.copy(deleteProducts = !it.deleteProducts) }
            is Inputs.SetIsDeleting -> updateState { it.copy(isDeleting = input.isDeleting) }
        }

    private suspend fun InputScope.handleDeleteGenerateClicked() {
        val state = getCurrentState()

        if (!state.deleteProducts && !state.deleteCustomers && !state.deleteOrders) {
            postEvent(Events.OnError("At least one field must be selected"))
            return
        }

        sideJob("handleDeleteGenerateClicked") {
            postInput(Inputs.SetIsDeleting(true))
            val input =
                DeleteGenerateDataInput(
                    products = state.deleteProducts,
                    users = state.deleteCustomers,
                    orders = state.deleteOrders,
                )
            adminService.deleteGeneratedData(input).fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                { postInput(Inputs.GetStats) },
            )
            postInput(Inputs.SetIsDeleting(false))
        }
    }

    private suspend fun InputScope.handleGenerateClicked() {
        val state = getCurrentState()

        val productsIsNumber = state.productsToGenerate.toString().toIntOrNull() != null
        val customersIsNumber = state.customersToGenerate.toString().toIntOrNull() != null
        val ordersIsNumber = state.ordersToGenerate.toString().toIntOrNull() != null

        if (!productsIsNumber || !customersIsNumber || !ordersIsNumber) {
            postEvent(Events.OnError("All fields must be numbers"))
            return
        }

        sideJob("handleGenerateClicked") {
            postInput(Inputs.SetIsGenerating(true))
            val input =
                GenerateDataInput(
                    products = state.productsToGenerate,
                    users = state.customersToGenerate,
                    orders = state.ordersToGenerate,
                )
            adminService.generateData(input).fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                { postInput(Inputs.GetStats) },
            )
            postInput(Inputs.SetIsGenerating(false))
        }
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(Inputs.GetStats)
        }
    }

    private suspend fun InputScope.handleGetStats() {
        sideJob("handleGetStats") {
            postInput(Inputs.SetIsStatsLoading(true))
            adminService.getStats().fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                { postInput(Inputs.SetStats(it.getStats)) },
            )
            postInput(Inputs.SetIsStatsLoading(false))
        }
    }
}

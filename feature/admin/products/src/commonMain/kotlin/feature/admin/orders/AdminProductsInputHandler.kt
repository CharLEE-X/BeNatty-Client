package feature.admin.orders

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AdminService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductsContract.Inputs, AdminProductsContract.Events, AdminProductsContract.State>

internal class AdminProductsInputHandler :
    KoinComponent,
    InputHandler<AdminProductsContract.Inputs, AdminProductsContract.Events, AdminProductsContract.State> {

    private val adminService: AdminService by inject()

    override suspend fun InputHandlerScope<AdminProductsContract.Inputs, AdminProductsContract.Events, AdminProductsContract.State>.handleInput(
        input: AdminProductsContract.Inputs,
    ) = when (input) {
        is AdminProductsContract.Inputs.GetProductsPage -> handleGetProductsPage(input.page)
        is AdminProductsContract.Inputs.SetProductsPage ->
            updateState { it.copy(products = input.products, info = input.info) }
    }

    private suspend fun InputScope.handleGetProductsPage(page: Int) {
        sideJob("handleGetProductsPage") {
            adminService.getProductsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminProductsContract.Inputs.SetProductsPage(
                            it.getProductsPage.products,
                            it.getProductsPage.info
                        )
                    )
                },
                onFailure = { postEvent(AdminProductsContract.Events.OnError(it.message ?: "Error fetching products")) }
            )
        }
    }
}

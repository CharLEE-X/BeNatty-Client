package feature.admin.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.ProductService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>

internal class AdminProductPageInputHandler :
    KoinComponent,
    InputHandler<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State> {

    private val productService by inject<ProductService>()

    override suspend fun InputHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>.handleInput(
        input: AdminProductPageContract.Inputs,
    ) = when (input) {
        is AdminProductPageContract.Inputs.GetProductsPage -> handleGetProductsPage(input.page)
        is AdminProductPageContract.Inputs.SetProductsPage ->
            updateState { it.copy(products = input.products, info = input.info) }
    }

    private suspend fun InputScope.handleGetProductsPage(page: Int) {
        sideJob("handleGetProductsPage") {
            productService.getProductsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminProductPageContract.Inputs.SetProductsPage(
                            it.getAllProductsPage.products,
                            it.getAllProductsPage.info
                        )
                    )
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(
                            it.message ?: "Error fetching products"
                        )
                    )
                }
            )
        }
    }
}

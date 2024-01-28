package feature.admin.product.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.ProductService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductListContract.Inputs, AdminProductListContract.Events, AdminProductListContract.State>

internal class AdminProductListInputHandler :
    KoinComponent,
    InputHandler<AdminProductListContract.Inputs, AdminProductListContract.Events, AdminProductListContract.State> {

    private val productService by inject<ProductService>()

    override suspend fun InputHandlerScope<AdminProductListContract.Inputs, AdminProductListContract.Events, AdminProductListContract.State>.handleInput(
        input: AdminProductListContract.Inputs,
    ) = when (input) {
        is AdminProductListContract.Inputs.GetProductsPage -> handleGetProductsPage(input.page)
        is AdminProductListContract.Inputs.SetProductsPage ->
            updateState { it.copy(products = input.products, info = input.info) }
    }

    private suspend fun InputScope.handleGetProductsPage(page: Int) {
        sideJob("handleGetProductsPage") {
            productService.getProductsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminProductListContract.Inputs.SetProductsPage(
                            it.getAllProductsPage.products,
                            it.getAllProductsPage.info
                        )
                    )
                },
                onFailure = {
                    postEvent(
                        AdminProductListContract.Events.OnError(
                            it.message ?: "Error fetching products"
                        )
                    )
                }
            )
        }
    }
}

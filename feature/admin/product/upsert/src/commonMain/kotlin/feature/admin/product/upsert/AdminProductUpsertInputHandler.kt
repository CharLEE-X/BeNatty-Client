package feature.admin.product.upsert

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.ProductService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductUpsertContract.Inputs, AdminProductUpsertContract.Events, AdminProductUpsertContract.State>

internal class AdminProductUpsertInputHandler :
    KoinComponent,
    InputHandler<AdminProductUpsertContract.Inputs, AdminProductUpsertContract.Events, AdminProductUpsertContract.State> {

    private val productService by inject<ProductService>()

    override suspend fun InputHandlerScope<AdminProductUpsertContract.Inputs, AdminProductUpsertContract.Events, AdminProductUpsertContract.State>.handleInput(
        input: AdminProductUpsertContract.Inputs,
    ) = when (input) {
        is AdminProductUpsertContract.Inputs.GetProductsPage -> handleGetProductsPage(input.page)
        is AdminProductUpsertContract.Inputs.SetProductsPage ->
            updateState { it.copy(products = input.products, info = input.info) }
    }

    private suspend fun InputScope.handleGetProductsPage(page: Int) {
        sideJob("handleGetProductsPage") {
            productService.getProductsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminProductUpsertContract.Inputs.SetProductsPage(
                            it.getAllProductsPage.products,
                            it.getAllProductsPage.info
                        )
                    )
                },
                onFailure = {
                    postEvent(
                        AdminProductUpsertContract.Events.OnError(
                            it.message ?: "Error fetching products"
                        )
                    )
                }
            )
        }
    }
}

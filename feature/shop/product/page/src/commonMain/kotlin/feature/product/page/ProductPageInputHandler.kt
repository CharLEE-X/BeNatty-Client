package feature.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.ProductService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State>

internal class ProductPageInputHandler :
    KoinComponent,
    InputHandler<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State> {

    private val productService by inject<ProductService>()

    override suspend fun InputScope.handleInput(input: ProductPageContract.Inputs) = when (input) {
        is ProductPageContract.Inputs.Init -> handleInit(input.productId)
        is ProductPageContract.Inputs.FetchProduct -> handleFetchProduct(input.productId)
        is ProductPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.loading) }
        is ProductPageContract.Inputs.SetProduct -> updateState { it.copy(product = input.product) }
    }

    private suspend fun InputScope.handleFetchProduct(productId: String) {
        sideJob("handleFetchProduct") {
            productService.getProductById(productId).fold(
                { postEvent(ProductPageContract.Events.OnError(it.toString())) },
                { postInput(ProductPageContract.Inputs.SetProduct(it.getProductById)) }
            )
        }
    }

    private suspend fun InputScope.handleInit(productId: String) {
        sideJob("handleInit") {
            postInput(ProductPageContract.Inputs.SetLoading(true))
            postInput(ProductPageContract.Inputs.FetchProduct(productId))
            postInput(ProductPageContract.Inputs.SetLoading(false))
        }
    }
}

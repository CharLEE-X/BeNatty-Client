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
    }

    private suspend fun InputScope.handleInit(productId: String?) {
        noOp()
    }
}

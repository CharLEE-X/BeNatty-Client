package feature.product.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ProductPageEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State> {
    override suspend fun EventHandlerScope<
        ProductPageContract.Inputs,
        ProductPageContract.Events,
        ProductPageContract.State
        >.handleEvent(event: ProductPageContract.Events) = when (event) {
        is ProductPageContract.Events.OnError -> onError(event.message)
    }
}

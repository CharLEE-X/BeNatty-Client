package feature.product.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ProductPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToProduct: suspend (String) -> Unit,
    private val openAskQuestionDialog: suspend () -> Unit,
    private val openSizeGuideDialog: suspend () -> Unit,
) : EventHandler<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State> {
    override suspend fun EventHandlerScope<
        ProductPageContract.Inputs,
        ProductPageContract.Events,
        ProductPageContract.State
        >.handleEvent(event: ProductPageContract.Events) = when (event) {
        is ProductPageContract.Events.OnError -> onError(event.message)
        is ProductPageContract.Events.GoToProduct -> goToProduct(event.productId)
        ProductPageContract.Events.OpenAskQuestionDialog -> openAskQuestionDialog()
        ProductPageContract.Events.OpenSizeGuideDialog -> openSizeGuideDialog()
    }
}

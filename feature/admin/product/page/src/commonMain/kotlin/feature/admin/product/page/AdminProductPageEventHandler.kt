package feature.admin.product.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToProductList: suspend () -> Unit,
) : EventHandler<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State> {
    override suspend fun EventHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>.handleEvent(
        event: AdminProductPageContract.Events,
    ) = when (event) {
        is AdminProductPageContract.Events.OnError -> onError(event.message)
        AdminProductPageContract.Events.GoToProductList -> goToProductList()
    }
}

package feature.admin.orders

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductsEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminProductsContract.Inputs, AdminProductsContract.Events, AdminProductsContract.State> {
    override suspend fun EventHandlerScope<AdminProductsContract.Inputs, AdminProductsContract.Events, AdminProductsContract.State>.handleEvent(
        event: AdminProductsContract.Events,
    ) = when (event) {
        is AdminProductsContract.Events.OnError -> onError(event.message)
    }
}

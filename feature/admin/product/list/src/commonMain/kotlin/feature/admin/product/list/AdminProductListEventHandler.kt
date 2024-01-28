package feature.admin.product.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductListEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminProductListContract.Inputs, AdminProductListContract.Events, AdminProductListContract.State> {
    override suspend fun EventHandlerScope<AdminProductListContract.Inputs, AdminProductListContract.Events, AdminProductListContract.State>.handleEvent(
        event: AdminProductListContract.Events,
    ) = when (event) {
        is AdminProductListContract.Events.OnError -> onError(event.message)
    }
}

package feature.admin.order.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminOrderPageEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminOrderPageContract.Inputs, AdminOrderPageContract.Events, AdminOrderPageContract.State> {
    override suspend fun EventHandlerScope<AdminOrderPageContract.Inputs, AdminOrderPageContract.Events, AdminOrderPageContract.State>.handleEvent(
        event: AdminOrderPageContract.Events,
    ) = when (event) {
        is AdminOrderPageContract.Events.OnError -> onError(event.message)
    }
}

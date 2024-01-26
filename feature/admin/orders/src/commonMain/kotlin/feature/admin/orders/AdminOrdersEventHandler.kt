package feature.admin.orders

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminOrdersEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminOrdersContract.Inputs, AdminOrdersContract.Events, AdminOrdersContract.State> {
    override suspend fun EventHandlerScope<AdminOrdersContract.Inputs, AdminOrdersContract.Events, AdminOrdersContract.State>.handleEvent(
        event: AdminOrdersContract.Events,
    ) = when (event) {
        is AdminOrdersContract.Events.OnError -> onError(event.message)
    }
}

package feature.admin.order.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminOrderListEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminOrderListContract.Inputs, AdminOrderListContract.Events, AdminOrderListContract.State> {
    override suspend fun EventHandlerScope<AdminOrderListContract.Inputs, AdminOrderListContract.Events, AdminOrderListContract.State>.handleEvent(
        event: AdminOrderListContract.Events,
    ) = when (event) {
        is AdminOrderListContract.Events.OnError -> onError(event.message)
    }
}

package feature.admin.order.upsert

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminOrderUpsertEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminOrderUpsertContract.Inputs, AdminOrderUpsertContract.Events, AdminOrderUpsertContract.State> {
    override suspend fun EventHandlerScope<AdminOrderUpsertContract.Inputs, AdminOrderUpsertContract.Events, AdminOrderUpsertContract.State>.handleEvent(
        event: AdminOrderUpsertContract.Events,
    ) = when (event) {
        is AdminOrderUpsertContract.Events.OnError -> onError(event.message)
    }
}

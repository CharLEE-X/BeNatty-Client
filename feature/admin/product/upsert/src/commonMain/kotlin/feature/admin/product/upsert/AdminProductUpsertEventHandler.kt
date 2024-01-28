package feature.admin.product.upsert

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductUpsertEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminProductUpsertContract.Inputs, AdminProductUpsertContract.Events, AdminProductUpsertContract.State> {
    override suspend fun EventHandlerScope<AdminProductUpsertContract.Inputs, AdminProductUpsertContract.Events, AdminProductUpsertContract.State>.handleEvent(
        event: AdminProductUpsertContract.Events,
    ) = when (event) {
        is AdminProductUpsertContract.Events.OnError -> onError(event.message)
    }
}

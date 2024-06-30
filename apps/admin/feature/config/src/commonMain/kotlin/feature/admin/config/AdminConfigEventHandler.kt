package feature.admin.config

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminConfigEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminConfigContract.Inputs, AdminConfigContract.Events, AdminConfigContract.State> {
    override suspend fun EventHandlerScope<
        AdminConfigContract.Inputs,
        AdminConfigContract.Events,
        AdminConfigContract.State,
        >.handleEvent(
        event: AdminConfigContract.Events,
    ) = when (event) {
        is AdminConfigContract.Events.OnError -> onError(event.message)
    }
}

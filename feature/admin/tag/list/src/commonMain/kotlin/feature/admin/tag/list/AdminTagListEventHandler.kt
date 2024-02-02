package feature.admin.tag.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminTagListEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminTagListContract.Inputs, AdminTagListContract.Events, AdminTagListContract.State> {
    override suspend fun EventHandlerScope<AdminTagListContract.Inputs, AdminTagListContract.Events, AdminTagListContract.State>.handleEvent(
        event: AdminTagListContract.Events,
    ) = when (event) {
        is AdminTagListContract.Events.OnError -> onError(event.message)
    }
}

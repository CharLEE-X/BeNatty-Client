package feature.admin.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import feature.admin.list.AdminListContract.Events
import feature.admin.list.AdminListContract.Inputs
import feature.admin.list.AdminListContract.State

internal class AdminListEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToCreate: () -> Unit,
    private val goToDetail: (String) -> Unit,
) : EventHandler<Inputs, Events, State> {
    override suspend fun EventHandlerScope<Inputs, Events, State>.handleEvent(event: Events) =
        when (event) {
            is Events.OnError -> onError(event.message)
            is Events.GoTo.Create -> goToCreate()
            is Events.GoTo.Detail -> goToDetail(event.id)
        }
}

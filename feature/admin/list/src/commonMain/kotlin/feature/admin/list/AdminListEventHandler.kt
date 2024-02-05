package feature.admin.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminListEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToCreate: () -> Unit,
    private val goToDetail: (String) -> Unit,
) : EventHandler<AdminListContract.Inputs, AdminListContract.Events, AdminListContract.State> {
    override suspend fun EventHandlerScope<AdminListContract.Inputs, AdminListContract.Events, AdminListContract.State>.handleEvent(
        event: AdminListContract.Events,
    ) = when (event) {
        is AdminListContract.Events.OnError -> onError(event.message)
        is AdminListContract.Events.GoTo.Create -> goToCreate()
        is AdminListContract.Events.GoTo.Detail -> goToDetail(event.id)
    }
}

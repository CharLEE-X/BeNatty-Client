package feature.admin.tag.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminTagEditEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: () -> Unit,
    private val goToUser: (String) -> Unit,
    private val goToTag: (String) -> Unit,
) : EventHandler<AdminTagEditContract.Inputs, AdminTagEditContract.Events, AdminTagEditContract.State> {
    override suspend fun EventHandlerScope<AdminTagEditContract.Inputs, AdminTagEditContract.Events, AdminTagEditContract.State>.handleEvent(
        event: AdminTagEditContract.Events,
    ) = when (event) {
        is AdminTagEditContract.Events.OnError -> onError(event.message)
        AdminTagEditContract.Events.GoBack -> goBack()
        is AdminTagEditContract.Events.GoToTag -> goToTag(event.id)
        is AdminTagEditContract.Events.GoToUser -> goToUser(event.id)
    }
}

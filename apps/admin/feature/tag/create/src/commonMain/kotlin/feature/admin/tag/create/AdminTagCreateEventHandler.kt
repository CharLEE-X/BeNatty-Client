package feature.admin.tag.create

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminTagCreateEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: () -> Unit,
    private val goToTag: (String) -> Unit,
) : EventHandler<AdminTagCreateContract.Inputs, AdminTagCreateContract.Events, AdminTagCreateContract.State> {
    override suspend fun EventHandlerScope<AdminTagCreateContract.Inputs, AdminTagCreateContract.Events, AdminTagCreateContract.State>.handleEvent(
        event: AdminTagCreateContract.Events,
    ) = when (event) {
        is AdminTagCreateContract.Events.OnError -> onError(event.message)
        AdminTagCreateContract.Events.GoBack -> goBack()
        is AdminTagCreateContract.Events.GoToTag -> goToTag(event.id)
    }
}

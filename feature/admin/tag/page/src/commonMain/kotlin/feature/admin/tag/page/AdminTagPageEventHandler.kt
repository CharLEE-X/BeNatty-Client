package feature.admin.tag.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminTagPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: () -> Unit,
    private val goToUser: (String) -> Unit,
    private val goToTag: (String) -> Unit,
) : EventHandler<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State> {
    override suspend fun EventHandlerScope<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State>.handleEvent(
        event: AdminTagPageContract.Events,
    ) = when (event) {
        is AdminTagPageContract.Events.OnError -> onError(event.message)
        AdminTagPageContract.Events.GoToList -> goToUserList()
        is AdminTagPageContract.Events.GoToUser -> goToUser(event.id)
        is AdminTagPageContract.Events.GoToTag -> goToTag(event.id)
    }
}

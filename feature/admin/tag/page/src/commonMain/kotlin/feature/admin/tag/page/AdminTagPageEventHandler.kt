package feature.admin.tag.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminTagPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: suspend () -> Unit,
) : EventHandler<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State> {
    override suspend fun EventHandlerScope<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State>.handleEvent(
        event: AdminTagPageContract.Events,
    ) = when (event) {
        is AdminTagPageContract.Events.OnError -> onError(event.message)
        AdminTagPageContract.Events.GoToUserList -> goToUserList()
    }
}

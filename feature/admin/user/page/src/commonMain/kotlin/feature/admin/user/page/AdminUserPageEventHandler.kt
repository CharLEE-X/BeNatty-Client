package feature.admin.user.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminUserPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: suspend () -> Unit,
) : EventHandler<AdminUserPageContract.Inputs, AdminUserPageContract.Events, AdminUserPageContract.State> {
    override suspend fun EventHandlerScope<AdminUserPageContract.Inputs, AdminUserPageContract.Events, AdminUserPageContract.State>.handleEvent(
        event: AdminUserPageContract.Events,
    ) = when (event) {
        is AdminUserPageContract.Events.OnError -> onError(event.message)
        AdminUserPageContract.Events.GoToUserList -> goToUserList()
    }
}

package feature.admin.users

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminUsersEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminUsersContract.Inputs, AdminUsersContract.Events, AdminUsersContract.State> {
    override suspend fun EventHandlerScope<AdminUsersContract.Inputs, AdminUsersContract.Events, AdminUsersContract.State>.handleEvent(
        event: AdminUsersContract.Events,
    ) = when (event) {
        is AdminUsersContract.Events.OnError -> onError(event.message)
    }
}

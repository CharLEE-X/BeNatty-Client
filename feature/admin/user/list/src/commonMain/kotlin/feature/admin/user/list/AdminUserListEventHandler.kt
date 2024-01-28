package feature.admin.user.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminUserListEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminUserListContract.Inputs, AdminUserListContract.Events, AdminUserListContract.State> {
    override suspend fun EventHandlerScope<AdminUserListContract.Inputs, AdminUserListContract.Events, AdminUserListContract.State>.handleEvent(
        event: AdminUserListContract.Events,
    ) = when (event) {
        is AdminUserListContract.Events.OnError -> onError(event.message)
    }
}

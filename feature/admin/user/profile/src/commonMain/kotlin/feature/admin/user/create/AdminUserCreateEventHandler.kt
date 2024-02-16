package feature.admin.user.create

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminUserCreateEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: suspend () -> Unit,
) : EventHandler<AdminUserCreateContract.Inputs, AdminUserCreateContract.Events, AdminUserCreateContract.State> {
    override suspend fun EventHandlerScope<AdminUserCreateContract.Inputs, AdminUserCreateContract.Events, AdminUserCreateContract.State>.handleEvent(
        event: AdminUserCreateContract.Events,
    ) = when (event) {
        is AdminUserCreateContract.Events.OnError -> onError(event.message)
        AdminUserCreateContract.Events.GoToUserList -> goToUserList()
    }
}

package feature.admin.dashboard

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminDashboardEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State> {
    override suspend fun EventHandlerScope<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State>.handleEvent(
        event: AdminDashboardContract.Events,
    ) = when (event) {
        is AdminDashboardContract.Events.OnError -> onError(event.message)
    }
}

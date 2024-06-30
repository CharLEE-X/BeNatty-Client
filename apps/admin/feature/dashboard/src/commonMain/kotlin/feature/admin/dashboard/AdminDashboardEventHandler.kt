package feature.admin.dashboard

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import feature.admin.dashboard.AdminDashboardContract.Events
import feature.admin.dashboard.AdminDashboardContract.Inputs
import feature.admin.dashboard.AdminDashboardContract.State

internal class AdminDashboardEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<Inputs, Events, State> {
    override suspend fun EventHandlerScope<Inputs, Events, State>.handleEvent(event: Events) =
        when (event) {
            is Events.OnError -> onError(event.message)
        }
}

package feature.admin.dashboard

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AdminService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias AdminInputScope = InputHandlerScope<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State>

internal class AdminDashboardInputHandler :
    KoinComponent,
    InputHandler<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State> {

    private val adminService: AdminService by inject()

    override suspend fun InputHandlerScope<AdminDashboardContract.Inputs, AdminDashboardContract.Events, AdminDashboardContract.State>.handleInput(
        input: AdminDashboardContract.Inputs,
    ) = when (input) {
        AdminDashboardContract.Inputs.GetStats -> handleGetStats()
        is AdminDashboardContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminDashboardContract.Inputs.SetStats -> updateState { it.copy(stats = input.stats) }
    }

    private suspend fun AdminInputScope.handleGetStats() {
        sideJob("handleGetStats") {
            postInput(AdminDashboardContract.Inputs.SetIsLoading(true))
            adminService.getStats().fold(
                { e ->
                    postEvent(AdminDashboardContract.Events.OnError(e.mapToUiMessage()))
                },
                { postInput(AdminDashboardContract.Inputs.SetStats(it.getStats)) },
            )
            postInput(AdminDashboardContract.Inputs.SetIsLoading(false))
        }
    }
}

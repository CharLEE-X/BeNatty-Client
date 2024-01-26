package feature.admin.users

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AdminService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias UsersInputScope = InputHandlerScope<AdminUsersContract.Inputs, AdminUsersContract.Events, AdminUsersContract.State>

internal class AdminUsersInputHandler :
    KoinComponent,
    InputHandler<AdminUsersContract.Inputs, AdminUsersContract.Events, AdminUsersContract.State> {

    private val adminService: AdminService by inject()

    override suspend fun InputHandlerScope<AdminUsersContract.Inputs, AdminUsersContract.Events, AdminUsersContract.State>.handleInput(
        input: AdminUsersContract.Inputs,
    ) = when (input) {
        is AdminUsersContract.Inputs.GetUsersPage -> handleGetUsersPage(input.page)
        is AdminUsersContract.Inputs.SetUsersPage -> updateState { it.copy(users = input.users, info = input.info) }
    }

    private suspend fun UsersInputScope.handleGetUsersPage(page: Int) {
        sideJob("handleGetUsersPage") {
            adminService.getUsersPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminUsersContract.Inputs.SetUsersPage(
                            it.getUsersPage.users,
                            it.getUsersPage.info
                        )
                    )
                },
                onFailure = { postEvent(AdminUsersContract.Events.OnError(it.message ?: "Error fetching users")) }
            )
        }
    }
}

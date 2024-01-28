package feature.admin.user.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias UsersInputScope = InputHandlerScope<AdminUserListContract.Inputs, AdminUserListContract.Events, AdminUserListContract.State>

internal class AdminUserListInputHandler :
    KoinComponent,
    InputHandler<AdminUserListContract.Inputs, AdminUserListContract.Events, AdminUserListContract.State> {

    private val userService: UserService by inject()

    override suspend fun InputHandlerScope<AdminUserListContract.Inputs, AdminUserListContract.Events, AdminUserListContract.State>.handleInput(
        input: AdminUserListContract.Inputs,
    ) = when (input) {
        is AdminUserListContract.Inputs.GetUsersPage -> handleGetUsersPage(input.page)
        is AdminUserListContract.Inputs.SetUsersPage -> updateState { it.copy(users = input.users, info = input.info) }
    }

    private suspend fun UsersInputScope.handleGetUsersPage(page: Int) {
        sideJob("handleGetUsersPage") {
            userService.getAllAsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminUserListContract.Inputs.SetUsersPage(
                            it.getAllUsersPage.users,
                            it.getAllUsersPage.info
                        )
                    )
                },
                onFailure = { postEvent(AdminUserListContract.Events.OnError(it.message ?: "Error fetching users")) }
            )
        }
    }
}

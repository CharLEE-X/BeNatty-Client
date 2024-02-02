package feature.admin.tag.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias UsersInputScope = InputHandlerScope<AdminTagListContract.Inputs, AdminTagListContract.Events, AdminTagListContract.State>

internal class AdminTagListInputHandler :
    KoinComponent,
    InputHandler<AdminTagListContract.Inputs, AdminTagListContract.Events, AdminTagListContract.State> {

    private val userService: UserService by inject()

    override suspend fun InputHandlerScope<AdminTagListContract.Inputs, AdminTagListContract.Events, AdminTagListContract.State>.handleInput(
        input: AdminTagListContract.Inputs,
    ) = when (input) {
        is AdminTagListContract.Inputs.GetUsersPage -> handleGetUsersPage(input.page)
        is AdminTagListContract.Inputs.SetUsersPage -> updateState { it.copy(users = input.users, info = input.info) }
    }

    private suspend fun UsersInputScope.handleGetUsersPage(page: Int) {
        sideJob("handleGetUsersPage") {
            userService.getAllAsPage(page, 10).fold(
                onSuccess = {
                    postInput(
                        AdminTagListContract.Inputs.SetUsersPage(
                            it.getAllUsersPage.users,
                            it.getAllUsersPage.info
                        )
                    )
                },
                onFailure = { postEvent(AdminTagListContract.Events.OnError(it.message ?: "Error fetching users")) }
            )
        }
    }
}

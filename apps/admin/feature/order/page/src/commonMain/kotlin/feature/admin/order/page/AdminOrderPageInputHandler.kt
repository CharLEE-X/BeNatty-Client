package feature.admin.order.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminOrderPageContract.Inputs, AdminOrderPageContract.Events, AdminOrderPageContract.State>

internal class AdminOrderPageInputHandler :
    KoinComponent,
    InputHandler<AdminOrderPageContract.Inputs, AdminOrderPageContract.Events, AdminOrderPageContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminOrderPageContract.Inputs, AdminOrderPageContract.Events, AdminOrderPageContract.State>.handleInput(
        input: AdminOrderPageContract.Inputs,
    ) = when (input) {
        is AdminOrderPageContract.Inputs.SetFullName -> {}
    }
}

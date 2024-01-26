package feature.admin.orders

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias OrdersInputScope = InputHandlerScope<AdminOrdersContract.Inputs, AdminOrdersContract.Events, AdminOrdersContract.State>

internal class AdminOrdersInputHandler :
    KoinComponent,
    InputHandler<AdminOrdersContract.Inputs, AdminOrdersContract.Events, AdminOrdersContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminOrdersContract.Inputs, AdminOrdersContract.Events, AdminOrdersContract.State>.handleInput(
        input: AdminOrdersContract.Inputs,
    ) = when (input) {
        is AdminOrdersContract.Inputs.SetFullName -> {}
    }
}

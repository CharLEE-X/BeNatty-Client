package feature.admin.order.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminOrderListContract.Inputs, AdminOrderListContract.Events, AdminOrderListContract.State>

internal class AdminOrderListInputHandler :
    KoinComponent,
    InputHandler<AdminOrderListContract.Inputs, AdminOrderListContract.Events, AdminOrderListContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminOrderListContract.Inputs, AdminOrderListContract.Events, AdminOrderListContract.State>.handleInput(
        input: AdminOrderListContract.Inputs,
    ) = when (input) {
        is AdminOrderListContract.Inputs.SetFullName -> {}
    }
}

package feature.shop.account.orders

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias OrdersInputScope = InputHandlerScope<OrdersContract.Inputs, OrdersContract.Events, OrdersContract.State>

internal class OrdersInputHandler :
    KoinComponent,
    InputHandler<OrdersContract.Inputs, OrdersContract.Events, OrdersContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<OrdersContract.Inputs, OrdersContract.Events, OrdersContract.State>.handleInput(
        input: OrdersContract.Inputs,
    ) = when (input) {
        is OrdersContract.Inputs.SetFullName -> {}
    }
}

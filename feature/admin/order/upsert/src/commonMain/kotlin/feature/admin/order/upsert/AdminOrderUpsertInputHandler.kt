package feature.admin.order.upsert

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminOrderUpsertContract.Inputs, AdminOrderUpsertContract.Events, AdminOrderUpsertContract.State>

internal class AdminOrderUpsertInputHandler :
    KoinComponent,
    InputHandler<AdminOrderUpsertContract.Inputs, AdminOrderUpsertContract.Events, AdminOrderUpsertContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminOrderUpsertContract.Inputs, AdminOrderUpsertContract.Events, AdminOrderUpsertContract.State>.handleInput(
        input: AdminOrderUpsertContract.Inputs,
    ) = when (input) {
        is AdminOrderUpsertContract.Inputs.SetFullName -> {}
    }
}

package feature.account.returns

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias ReturnsInputScope = InputHandlerScope<ReturnsContract.Inputs, ReturnsContract.Events, ReturnsContract.State>

internal class ReturnsInputHandler :
    KoinComponent,
    InputHandler<ReturnsContract.Inputs, ReturnsContract.Events, ReturnsContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<ReturnsContract.Inputs, ReturnsContract.Events, ReturnsContract.State>.handleInput(
        input: ReturnsContract.Inputs,
    ) = when (input) {
        is ReturnsContract.Inputs.SetEmail -> {}
    }
}

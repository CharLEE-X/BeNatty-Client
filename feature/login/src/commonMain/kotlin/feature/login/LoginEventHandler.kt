package feature.login

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class LoginEventHandler(
    private val onError: suspend (String) -> Unit,
    private val onAuthenticated: () -> Unit,
) : EventHandler<LoginContract.Inputs, LoginContract.Events, LoginContract.State> {
    override suspend fun EventHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>.handleEvent(
        event: LoginContract.Events,
    ) = when (event) {
        is LoginContract.Events.OnError -> onError(event.message)
        LoginContract.Events.OnAuthenticated -> onAuthenticated()
    }
}

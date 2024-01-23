package feature.login

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class LoginEventHandler(
    private val onError: suspend (String) -> Unit,
    private val onAuthenticated: () -> Unit,
    private val gotoPrivacyPolicy: () -> Unit,
    private val gotoTnC: () -> Unit,
) : EventHandler<LoginContract.Inputs, LoginContract.Events, LoginContract.State> {
    override suspend fun EventHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>.handleEvent(
        event: LoginContract.Events,
    ) = when (event) {
        is LoginContract.Events.OnError -> onError(event.message)
        LoginContract.Events.OnAuthenticated -> onAuthenticated()
        LoginContract.Events.GotoPrivacyPolicy -> gotoPrivacyPolicy()
        LoginContract.Events.GotoTnC -> gotoTnC()
    }
}

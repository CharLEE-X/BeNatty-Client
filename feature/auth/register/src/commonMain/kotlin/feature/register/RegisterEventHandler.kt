package feature.register

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class RegisterEventHandler(
    private val onError: suspend (String) -> Unit,
    private val onAuthenticated: () -> Unit,
    private val goToLogin: () -> Unit,
    private val gotoPrivacyPolicy: () -> Unit,
    private val gotoTnC: () -> Unit,
) : EventHandler<RegisterContract.Inputs, RegisterContract.Events, RegisterContract.State> {
    override suspend fun EventHandlerScope<RegisterContract.Inputs, RegisterContract.Events, RegisterContract.State>.handleEvent(
        event: RegisterContract.Events,
    ) = when (event) {
        is RegisterContract.Events.OnError -> onError(event.message)
        RegisterContract.Events.OnAuthenticated -> onAuthenticated()
        RegisterContract.Events.GoToPrivacyPolicy -> gotoPrivacyPolicy()
        RegisterContract.Events.GoToTnC -> gotoTnC()
        RegisterContract.Events.GoToLogin -> goToLogin()
    }
}

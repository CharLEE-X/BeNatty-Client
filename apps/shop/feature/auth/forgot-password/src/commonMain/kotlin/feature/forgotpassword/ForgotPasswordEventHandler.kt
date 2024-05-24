package feature.forgotpassword

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ForgotPasswordEventHandler(
    private val goToLogin: () -> Unit,
) : EventHandler<ForgotPasswordContract.Inputs, ForgotPasswordContract.Events, ForgotPasswordContract.State> {
    override suspend fun EventHandlerScope<ForgotPasswordContract.Inputs, ForgotPasswordContract.Events, ForgotPasswordContract.State>.handleEvent(
        event: ForgotPasswordContract.Events,
    ) = when (event) {
        ForgotPasswordContract.Events.GoToLogin -> goToLogin()
    }
}

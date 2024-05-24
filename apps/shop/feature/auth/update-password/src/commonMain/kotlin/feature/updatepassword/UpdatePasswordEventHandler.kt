package feature.updatepassword

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class UpdatePasswordEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToLogin: () -> Unit,
) : EventHandler<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State> {
    override suspend fun EventHandlerScope<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State>.handleEvent(
        event: UpdatePasswordContract.Events,
    ) = when (event) {
        is UpdatePasswordContract.Events.OnError -> onError(event.message)
        UpdatePasswordContract.Events.GoToLogin -> goToLogin()
    }
}

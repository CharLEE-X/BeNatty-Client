package feature.account.profile

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ProfileEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State> {
    override suspend fun EventHandlerScope<ProfileContract.Inputs, ProfileContract.Events, ProfileContract.State>.handleEvent(
        event: ProfileContract.Events,
    ) = when (event) {
        is ProfileContract.Events.OnError -> onError(event.message)
    }
}

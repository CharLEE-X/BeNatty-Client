package feature.account.returns

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ReturnsEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<ReturnsContract.Inputs, ReturnsContract.Events, ReturnsContract.State> {
    override suspend fun EventHandlerScope<ReturnsContract.Inputs, ReturnsContract.Events, ReturnsContract.State>.handleEvent(
        event: ReturnsContract.Events,
    ) = when (event) {
        is ReturnsContract.Events.OnError -> onError(event.message)
    }
}

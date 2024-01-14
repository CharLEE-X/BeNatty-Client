package feature.debug

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class DebugEventHandler(
    private val onError: suspend (String) -> Unit,
    private val onGenerateUsersSuccess: suspend (String) -> Unit,
    private val onDeleteUsersSuccess: suspend (String) -> Unit,
) : EventHandler<DebugContract.Inputs, DebugContract.Events, DebugContract.State> {
    override suspend fun EventHandlerScope<DebugContract.Inputs, DebugContract.Events, DebugContract.State>.handleEvent(
        event: DebugContract.Events,
    ) = when (event) {
        is DebugContract.Events.OnError -> onError(event.message)
        is DebugContract.Events.OnGenerateUsersSuccess -> onGenerateUsersSuccess(event.message)
        is DebugContract.Events.OnDeleteUsersSuccess -> onDeleteUsersSuccess(event.message)
    }
}

package feature.root

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias RootEventHandlerScope =
    EventHandlerScope<RootContract.Inputs, RootContract.Events, RootContract.State>

internal class RootEventHandler(
    private val onError: suspend (String) -> Unit,
) : KoinComponent, EventHandler<RootContract.Inputs, RootContract.Events, RootContract.State> {
    override suspend fun RootEventHandlerScope.handleEvent(event: RootContract.Events) = when (event) {
        is RootContract.Events.OnError -> onError(event.message)
    }
}

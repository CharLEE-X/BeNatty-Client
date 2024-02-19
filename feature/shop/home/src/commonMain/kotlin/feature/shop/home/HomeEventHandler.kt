package feature.shop.home

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class HomeEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<HomeContract.Inputs, HomeContract.Events, HomeContract.State> {
    override suspend fun EventHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleEvent(
        event: HomeContract.Events,
    ) = when (event) {
        is HomeContract.Events.OnError -> onError(event.message)
    }
}

package web.components.sections.desktopNav

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias RootEventHandlerScope =
    EventHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>

internal class DesktopNavEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToOrders: () -> Unit,
    private val goToAccount: () -> Unit,
    private val goToReturns: () -> Unit,
    private val goToWishlist: () -> Unit,
    private val logOut: () -> Unit,
) : KoinComponent, EventHandler<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State> {

    override suspend fun RootEventHandlerScope.handleEvent(
        event: DesktopNavContract.Events,
    ) = when (event) {
        is DesktopNavContract.Events.OnError -> onError(event.message)
        DesktopNavContract.Events.GoToOrders -> goToOrders()
        DesktopNavContract.Events.GoToAccount -> goToAccount()
        DesktopNavContract.Events.GoToReturns -> goToReturns()
        DesktopNavContract.Events.GoToWishlist -> goToWishlist()
        DesktopNavContract.Events.LogOut -> logOut()
    }
}

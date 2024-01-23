package web.components.sections.desktopNav

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import org.koin.core.component.KoinComponent

private typealias DesktopNavInput =
    InputHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>

internal class DesktopNavInputHandler :
    KoinComponent,
    InputHandler<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State> {

    override suspend fun InputHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>.handleInput(
        input: DesktopNavContract.Inputs,
    ) = when (input) {
        is DesktopNavContract.Inputs.OnAccountMenuItemClicked -> handleAccountMenuItemClicked(input.item)
    }

    private suspend fun DesktopNavInput.handleAccountMenuItemClicked(item: DesktopNavContract.AccountMenuItem) {
        when (item) {
            DesktopNavContract.AccountMenuItem.ORDERS -> {
                postEvent(DesktopNavContract.Events.GoToOrders)
            }

            DesktopNavContract.AccountMenuItem.RETURNS -> {
                postEvent(DesktopNavContract.Events.GoToReturns)
            }

            DesktopNavContract.AccountMenuItem.WISHLIST -> {
                postEvent(DesktopNavContract.Events.GoToWishlist)
            }

            DesktopNavContract.AccountMenuItem.ACCOUNT -> {
                postEvent(DesktopNavContract.Events.GoToAccount)
            }

            DesktopNavContract.AccountMenuItem.LOGOUT -> {
                postEvent(DesktopNavContract.Events.LogOut)
            }
        }
    }
}

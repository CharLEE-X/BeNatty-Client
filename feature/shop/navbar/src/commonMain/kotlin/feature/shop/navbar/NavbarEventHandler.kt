package feature.shop.navbar

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias RootEventHandlerScope =
    EventHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>

internal class NavbarEventHandler(
    private val onError: suspend (String) -> Unit,
    private val desktopNavRoutes: DesktopNavRoutes,
    private val showCartSidebar: (Boolean) -> Unit,
) : KoinComponent, EventHandler<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State> {
    override suspend fun RootEventHandlerScope.handleEvent(
        event: DesktopNavContract.Events,
    ) = when (event) {
        is DesktopNavContract.Events.OnError -> onError(event.message)
        DesktopNavContract.Events.GoToOrders -> desktopNavRoutes.goToOrders()
        DesktopNavContract.Events.GoToProfile -> desktopNavRoutes.goToProfile()
        DesktopNavContract.Events.GoToReturns -> desktopNavRoutes.goToReturns()
        DesktopNavContract.Events.GoToWishlist -> desktopNavRoutes.goToWishlist()
        DesktopNavContract.Events.GoToLogin -> desktopNavRoutes.goToLogin()
        DesktopNavContract.Events.GoToHome -> desktopNavRoutes.goToHome()
        DesktopNavContract.Events.GoToHelpAndFAQ -> desktopNavRoutes.goToHelpAndFaq()
        DesktopNavContract.Events.GoToCatalogue -> desktopNavRoutes.goToCatalogue()
        DesktopNavContract.Events.GoToAbout -> desktopNavRoutes.goToAbout()
        DesktopNavContract.Events.GoToShippingAndReturns -> desktopNavRoutes.goToShippingAndReturns()
        is DesktopNavContract.Events.ShowCartSidebar -> showCartSidebar(event.showCartSidebar)
    }
}

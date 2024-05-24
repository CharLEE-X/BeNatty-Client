package feature.shop.navbar

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias RootEventHandlerScope =
    EventHandlerScope<NavbarContract.Inputs, NavbarContract.Events, NavbarContract.State>

internal class NavbarEventHandler(
    private val onError: suspend (String) -> Unit,
    private val desktopNavRoutes: DesktopNavRoutes,
    private val showCartSidebar: (Boolean) -> Unit,
    private val goToProductDetail: suspend (String) -> Unit,
) : KoinComponent, EventHandler<NavbarContract.Inputs, NavbarContract.Events, NavbarContract.State> {
    override suspend fun RootEventHandlerScope.handleEvent(event: NavbarContract.Events) = when (event) {
        is NavbarContract.Events.OnError -> onError(event.message)
        NavbarContract.Events.GoToOrders -> desktopNavRoutes.goToOrders()
        NavbarContract.Events.GoToProfile -> desktopNavRoutes.goToProfile()
        NavbarContract.Events.GoToReturns -> desktopNavRoutes.goToReturns()
        NavbarContract.Events.GoToWishlist -> desktopNavRoutes.goToWishlist()
        NavbarContract.Events.GoToLogin -> desktopNavRoutes.goToLogin()
        NavbarContract.Events.GoToHome -> desktopNavRoutes.goToHome()
        NavbarContract.Events.GoToHelpAndFAQ -> desktopNavRoutes.goToHelpAndFaq()
        NavbarContract.Events.GoToCatalogue -> desktopNavRoutes.goToCatalogue()
        NavbarContract.Events.GoToAbout -> desktopNavRoutes.goToAbout()
        NavbarContract.Events.GoToShippingAndReturns -> desktopNavRoutes.goToShippingAndReturns()
        is NavbarContract.Events.ShowCartSidebar -> showCartSidebar(event.showCartSidebar)
        is NavbarContract.Events.GoToProductDetail -> goToProductDetail(event.productId)
    }
}

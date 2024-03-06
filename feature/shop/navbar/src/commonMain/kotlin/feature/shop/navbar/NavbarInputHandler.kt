package feature.shop.navbar

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope =
    InputHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>

internal class NavbarInputHandler :
    KoinComponent,
    InputHandler<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<DesktopNavContract.Inputs, DesktopNavContract.Events, DesktopNavContract.State>.handleInput(
        input: DesktopNavContract.Inputs,
    ) = when (input) {
        DesktopNavContract.Inputs.Init -> handleInit()
        is DesktopNavContract.Inputs.CheckAuth -> handleCheckAuth()

        is DesktopNavContract.Inputs.OnAccountMenuItemSelected -> handleAccountMenuItemClicked(input.item)
        is DesktopNavContract.Inputs.OnStoreMenuItemSelected -> handleOnStoreMenuItemSelected(input.item)
        is DesktopNavContract.Inputs.OnSearchValueChanged -> updateState { it.copy(searchValue = input.value) }

        DesktopNavContract.Inputs.OnSearchEnterPress -> handleOnSearchEnterPress()
        DesktopNavContract.Inputs.OnCartClick -> handleOnCartClick()
        DesktopNavContract.Inputs.OnLoginClick -> handleOnLoginClick()
        DesktopNavContract.Inputs.OnHelpAndFAQClick -> postEvent(DesktopNavContract.Events.GoToHelpAndFAQ)
        DesktopNavContract.Inputs.OnLogoClick -> postEvent(DesktopNavContract.Events.GoToHome)
        DesktopNavContract.Inputs.OnWishlistClick -> postEvent(DesktopNavContract.Events.GoToWishlist)
        DesktopNavContract.Inputs.OnTickerClick -> postEvent(DesktopNavContract.Events.GoToCatalogue)
        DesktopNavContract.Inputs.OnStoreClick -> postEvent(DesktopNavContract.Events.GoToCatalogue)
        DesktopNavContract.Inputs.OnAboutClick -> postEvent(DesktopNavContract.Events.GoToAbout)
        DesktopNavContract.Inputs.OnShippingAndReturnsClick -> postEvent(DesktopNavContract.Events.GoToShippingAndReturns)
        DesktopNavContract.Inputs.OnProfileClick -> postEvent(DesktopNavContract.Events.GoToProfile)
        DesktopNavContract.Inputs.OnBasketClick -> postEvent(DesktopNavContract.Events.ShowCartSidebar(true))

        is DesktopNavContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is DesktopNavContract.Inputs.SetBasketCount -> updateState { it.copy(basketCount = input.count) }
    }

    private suspend fun InputScope.handleOnSearchEnterPress() {
        postEvent(DesktopNavContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnStoreMenuItemSelected(item: String) {
        postEvent(DesktopNavContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnCartClick() {
        noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(DesktopNavContract.Inputs.SetIsLoading(isLoading = true))
            postInput(DesktopNavContract.Inputs.CheckAuth)
            delay(5000)
            postInput(DesktopNavContract.Inputs.SetIsLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleCheckAuth() {
        val isAuthenticated = authService.isAuth()
        updateState { it.copy(isAuthenticated = isAuthenticated) }
    }

    private suspend fun InputScope.handleOnLoginClick() {
        if (getCurrentState().isAuthenticated) {
            postEvent(DesktopNavContract.Events.GoToProfile)
        } else {
            postEvent(DesktopNavContract.Events.GoToLogin)
        }
    }

    private suspend fun InputScope.handleAccountMenuItemClicked(item: DesktopNavContract.AccountMenuItem) {
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

            DesktopNavContract.AccountMenuItem.PROFILE -> {
                postEvent(DesktopNavContract.Events.GoToProfile)
            }

            DesktopNavContract.AccountMenuItem.LOGOUT -> {
                postEvent(DesktopNavContract.Events.GoToLogin)
            }
        }
    }
}

package feature.shop.navbar

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import data.service.ProductService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope =
    InputHandlerScope<NavbarContract.Inputs, NavbarContract.Events, NavbarContract.State>

internal class NavbarInputHandler :
    KoinComponent,
    InputHandler<NavbarContract.Inputs, NavbarContract.Events, NavbarContract.State> {

    private val authService: AuthService by inject()
    private val productService: ProductService by inject()

    override suspend fun InputHandlerScope<NavbarContract.Inputs, NavbarContract.Events, NavbarContract.State>.handleInput(
        input: NavbarContract.Inputs,
    ) = when (input) {
        NavbarContract.Inputs.Init -> handleInit()
        is NavbarContract.Inputs.CheckAuth -> handleCheckAuth()
        NavbarContract.Inputs.FetchRecommendedProducts -> handleFetchRecommendedProducts()

        is NavbarContract.Inputs.OnAccountMenuItemSelected -> handleAccountMenuItemClicked(input.item)
        is NavbarContract.Inputs.OnStoreMenuItemSelected -> handleOnStoreMenuItemSelected(input.item)
        is NavbarContract.Inputs.OnSearchValueChanged -> updateState { it.copy(searchValue = input.value) }

        NavbarContract.Inputs.OnSearchEnterPress -> handleOnSearchEnterPress()
        NavbarContract.Inputs.OnCartClick -> handleOnCartClick()
        NavbarContract.Inputs.OnLoginClick -> handleOnLoginClick()
        NavbarContract.Inputs.OnLogoClick -> postEvent(NavbarContract.Events.GoToHome)
        NavbarContract.Inputs.OnWishlistClick -> postEvent(NavbarContract.Events.GoToWishlist)
        NavbarContract.Inputs.OnStoreClicked -> postEvent(NavbarContract.Events.GoToCatalogue)
        NavbarContract.Inputs.OnExploreClicked -> postEvent(NavbarContract.Events.GoToAbout)

        is NavbarContract.Inputs.SetCheckAuthLoading -> updateState { it.copy(isCheckAuthLoading = input.isLoading) }
        NavbarContract.Inputs.OnPromoLeftClicked -> handleOnPromoLeftClicked()
        NavbarContract.Inputs.OnPromoMiddleClicked -> handleOnPromoMiddleClicked()
        NavbarContract.Inputs.OnPromoRightClicked -> handleOnPromoRightClicked()
        NavbarContract.Inputs.OnAllCollectionsClicked -> handleOnCollectionsClicked()
        NavbarContract.Inputs.OnOurFavouritesClicked -> handleOnOurFavouritesClicked()
        NavbarContract.Inputs.OnNewArrivalsClicked -> handleOnNewArrivalsClicked()
        NavbarContract.Inputs.OnSummerDealsClicked -> handleOnSummerDealsClicked()

        is NavbarContract.Inputs.SetIsRecommendedProductsLoading ->
            updateState { it.copy(isRecommendedProductsLoading = input.isLoading) }

        is NavbarContract.Inputs.SetRecommendedProducts -> updateState { it.copy(recommendedProducts = input.products) }
        is NavbarContract.Inputs.OnRecommendedProductClicked -> handleOnRecommendedProductClicked(input.id)
        NavbarContract.Inputs.OnBottomsClicked -> handleBottoms()
        NavbarContract.Inputs.OnContactClicked -> handleContact()
        NavbarContract.Inputs.OnDeliveryClicked -> handleDelivery()
        NavbarContract.Inputs.OnDressesClicked -> handleDresses()
        NavbarContract.Inputs.OnCollectionsClicked -> handleOnCollectionsClicked()
        NavbarContract.Inputs.OnShopTheLatestClicked -> handleOnShopTheLatestClicked()
        NavbarContract.Inputs.OnWeLoveClicked -> handleOnWeLoveClicked()
        NavbarContract.Inputs.OnReturnsClicked -> handleReturns()
        NavbarContract.Inputs.OnTopsClicked -> handleTops()
        NavbarContract.Inputs.OnSearchClicked -> handleOnSearchClicked()
        NavbarContract.Inputs.OnUserClicked -> handleOnLoginClick()
        is NavbarContract.Inputs.SetIsAuthenticated -> updateState { it.copy(isAuthenticated = input.authenticated) }
        NavbarContract.Inputs.OnCustomerServiceClicked -> handleOnCustomerServiceClicked()
    }

    private suspend fun InputScope.handleOnCustomerServiceClicked() {
        // FIXME: Implement this
        noOp()
    }

    private suspend fun InputScope.handleOnSearchClicked() {
        // FIXME: Implement this
        noOp()
    }

    private suspend fun InputScope.handleTops() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleReturns() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnWeLoveClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnShopTheLatestClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleDresses() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleDelivery() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleContact() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleBottoms() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnRecommendedProductClicked(id: String) {
        postEvent(NavbarContract.Events.GoToProductDetail(id))
    }

    private suspend fun InputScope.handleFetchRecommendedProducts() {
        sideJob("FetchRecommendedProducts") {
            postInput(NavbarContract.Inputs.SetIsRecommendedProductsLoading(true))
            productService.getRecommendedProducts().fold(
                { postEvent(NavbarContract.Events.OnError(it.toString())) },
                { postInput(NavbarContract.Inputs.SetRecommendedProducts(it.getRecommendedProducts)) }
            )
            postInput(NavbarContract.Inputs.SetIsRecommendedProductsLoading(false))
        }
    }

    private suspend fun InputScope.handleOnSummerDealsClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnNewArrivalsClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnPromoLeftClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnPromoMiddleClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnPromoRightClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnCollectionsClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnOurFavouritesClicked() {
        // FIXME: Implement this
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnSearchEnterPress() {
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnStoreMenuItemSelected(item: String) {
        postEvent(NavbarContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleOnCartClick() {
        noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("InitNavbar") {
            postInput(NavbarContract.Inputs.CheckAuth)
            postInput(NavbarContract.Inputs.FetchRecommendedProducts)
        }
    }

    private suspend fun InputScope.handleCheckAuth() {
        sideJob("CheckAuth") {
            postInput(NavbarContract.Inputs.SetIsAuthenticated(authService.isAuth()))
            postInput(NavbarContract.Inputs.SetCheckAuthLoading(false))
        }
    }

    private suspend fun InputScope.handleOnLoginClick() {
        if (getCurrentState().isAuthenticated) {
            postEvent(NavbarContract.Events.GoToProfile)
        } else {
            postEvent(NavbarContract.Events.GoToLogin)
        }
    }

    private suspend fun InputScope.handleAccountMenuItemClicked(item: NavbarContract.AccountMenuItem) {
        when (item) {
            NavbarContract.AccountMenuItem.ORDERS -> {
                postEvent(NavbarContract.Events.GoToOrders)
            }

            NavbarContract.AccountMenuItem.RETURNS -> {
                postEvent(NavbarContract.Events.GoToReturns)
            }

            NavbarContract.AccountMenuItem.WISHLIST -> {
                postEvent(NavbarContract.Events.GoToWishlist)
            }

            NavbarContract.AccountMenuItem.PROFILE -> {
                postEvent(NavbarContract.Events.GoToProfile)
            }

            NavbarContract.AccountMenuItem.LOGOUT -> {
                postEvent(NavbarContract.Events.GoToLogin)
            }
        }
    }
}

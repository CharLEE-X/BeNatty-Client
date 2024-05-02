package feature.shop.navbar

import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.GetRecommendedProductsQuery
import org.koin.core.component.KoinComponent

object NavbarContract : KoinComponent {
    data class State(
        val isCheckAuthLoading: Boolean = true,
        val isRecommendedProductsLoading: Boolean = true,

        val isAuthenticated: Boolean = false,
        val storeMenuItems: List<String> = listOf(
            getString(Strings.Woman),
            getString(Strings.Man),
            getString(Strings.Sale)
        ).map { it.uppercase() },
        val searchValue: String = "",

        val recommendedProducts: List<GetRecommendedProductsQuery.GetRecommendedProduct> = emptyList(),
        val currency: Currency = Currency("£", "GBP"),
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object CheckAuth : Inputs
        data object FetchRecommendedProducts : Inputs

        data object OnPromoLeftClicked : Inputs
        data object OnPromoMiddleClicked : Inputs
        data object OnPromoRightClicked : Inputs
        data class OnSearchValueChanged(val value: String) : Inputs
        data class OnAccountMenuItemSelected(val item: AccountMenuItem) : Inputs
        data class OnStoreMenuItemSelected(val item: String) : Inputs
        data object OnAllCollectionsClicked : Inputs
        data object OnOurFavouritesClicked : Inputs
        data object OnNewArrivalsClicked : Inputs
        data object OnSummerDealsClicked : Inputs
        data class OnRecommendedProductClicked(val id: String) : Inputs
        data object OnCustomerServiceClicked : Inputs

        data object OnSearchEnterPress : Inputs
        data object OnLogoClick : Inputs
        data object OnLoginClick : Inputs
        data object OnWishlistClick : Inputs
        data object OnCartClick : Inputs
        data object OnStoreClicked : Inputs
        data object OnExploreClicked : Inputs

        data object OnShopTheLatestClicked : Inputs
        data object OnWeLoveClicked : Inputs
        data object OnCollectionsClicked : Inputs
        data object OnTopsClicked : Inputs
        data object OnBottomsClicked : Inputs
        data object OnDressesClicked : Inputs
        data object OnDeliveryClicked : Inputs
        data object OnReturnsClicked : Inputs
        data object OnContactClicked : Inputs
        data object OnSearchClicked : Inputs
        data object OnUserClicked : Inputs

        data class SetCheckAuthLoading(val isLoading: Boolean) : Inputs
        data class SetIsAuthenticated(val authenticated: Boolean) : Inputs
        data class SetIsRecommendedProductsLoading(val isLoading: Boolean) : Inputs
        data class SetRecommendedProducts(
            val products: List<GetRecommendedProductsQuery.GetRecommendedProduct>
        ) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToOrders : Events
        data object GoToReturns : Events
        data object GoToWishlist : Events
        data object GoToProfile : Events
        data object GoToLogin : Events
        data object GoToHome : Events
        data object GoToHelpAndFAQ : Events
        data object GoToCatalogue : Events
        data object GoToAbout : Events
        data object GoToShippingAndReturns : Events
        data class ShowCartSidebar(val showCartSidebar: Boolean) : Events
        data class GoToProductDetail(val productId: String) : Events
    }

    enum class AccountMenuItem {
        ORDERS,
        RETURNS,
        WISHLIST,
        PROFILE,
        LOGOUT,
    }
}

data class DesktopNavRoutes(
    val goToHome: () -> Unit,
    val goToLogin: () -> Unit,
    val goToOrders: () -> Unit,
    val goToProfile: () -> Unit,
    val goToReturns: () -> Unit,
    val goToWishlist: () -> Unit,
    val goToHelpAndFaq: () -> Unit,
    val goToCatalogue: () -> Unit,
    val goToAbout: () -> Unit,
    val goToShippingAndReturns: () -> Unit,
    val goToProductDetail: (String) -> Unit,
)

fun NavbarContract.AccountMenuItem.label(): String {
    return when (this) {
        NavbarContract.AccountMenuItem.ORDERS -> getString(Strings.Orders)
        NavbarContract.AccountMenuItem.RETURNS -> getString(Strings.Returns)
        NavbarContract.AccountMenuItem.WISHLIST -> getString(Strings.Wishlist)
        NavbarContract.AccountMenuItem.PROFILE -> getString(Strings.Profile)
        NavbarContract.AccountMenuItem.LOGOUT -> getString(Strings.Logout)
    }
}

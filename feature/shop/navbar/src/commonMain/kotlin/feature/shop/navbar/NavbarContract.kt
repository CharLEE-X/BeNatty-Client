package feature.shop.navbar

import component.localization.getString
import org.koin.core.component.KoinComponent

object DesktopNavContract : KoinComponent {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = true,
        val isAuthenticated: Boolean = false,
        val storeMenuItems: List<String> = listOf(strings.woman, strings.man, strings.sale).map { it.uppercase() },
        val searchValue: String = "",
        val basketCount: Int = 0,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object CheckAuth : Inputs

        data class OnSearchValueChanged(val value: String) : Inputs
        data class OnAccountMenuItemSelected(val item: AccountMenuItem) : Inputs
        data class OnStoreMenuItemSelected(val item: String) : Inputs

        data object OnSearchEnterPress : Inputs
        data object OnHelpAndFAQClick : Inputs
        data object OnLogoClick : Inputs
        data object OnLoginClick : Inputs
        data object OnWishlistClick : Inputs
        data object OnCartClick : Inputs
        data object OnTickerClick : Inputs
        data object OnStoreClick : Inputs
        data object OnAboutClick : Inputs
        data object OnShippingAndReturnsClick : Inputs
        data object OnProfileClick : Inputs
        data object OnBasketClick : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetBasketCount(val count: Int) : Inputs
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
    }

    data class Strings(
        val ticker: String = getString(component.localization.Strings.Ticker),
        val helpAndFaq: String = getString(component.localization.Strings.HelpAndFaq),
        val search: String = getString(component.localization.Strings.Search),
        val login: String = getString(component.localization.Strings.Login),
        val orders: String = getString(component.localization.Strings.Orders),
        val returns: String = getString(component.localization.Strings.Returns),
        val wishlist: String = getString(component.localization.Strings.Wishlist),
        val profile: String = getString(component.localization.Strings.Profile),
        val logout: String = getString(component.localization.Strings.Logout),
        val store: String = getString(component.localization.Strings.Store),
        val woman: String = getString(component.localization.Strings.Woman),
        val man: String = getString(component.localization.Strings.Man),
        val shippingReturns: String = getString(component.localization.Strings.ShippingReturns),
        val about: String = getString(component.localization.Strings.About),
        val sale: String = getString(component.localization.Strings.Sale),
    ) {
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
    val showCartSidebar: (Boolean) -> Unit,
)

fun DesktopNavContract.AccountMenuItem.label(): String {
    return when (this) {
        DesktopNavContract.AccountMenuItem.ORDERS -> DesktopNavContract.State().strings.orders
        DesktopNavContract.AccountMenuItem.RETURNS -> DesktopNavContract.State().strings.returns
        DesktopNavContract.AccountMenuItem.WISHLIST -> DesktopNavContract.State().strings.wishlist
        DesktopNavContract.AccountMenuItem.PROFILE -> DesktopNavContract.State().strings.profile
        DesktopNavContract.AccountMenuItem.LOGOUT -> DesktopNavContract.State().strings.logout
    }
}

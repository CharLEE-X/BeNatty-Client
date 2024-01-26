package web.components.sections.desktopNav

import component.localization.getString
import org.koin.core.component.KoinComponent

object DesktopNavContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,
        val accountLoginButtonText: String = Strings.login,
    )

    sealed interface Inputs {
        data class OnAccountMenuItemClicked(val item: AccountMenuItem) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToOrders : Events
        data object GoToReturns : Events
        data object GoToWishlist : Events
        data object GoToAccount : Events
        data object LogOut : Events
    }

    object Strings {
        val promoText = getString(component.localization.Strings.PromoText)
        val helpAndFaq = getString(component.localization.Strings.HelpAndFaq)
        val currencyEnUs = getString(component.localization.Strings.CurrencyEnUs)
        val search = getString(component.localization.Strings.Search)
        val login = getString(component.localization.Strings.Login)
        val orders = getString(component.localization.Strings.Orders)
        val returns = getString(component.localization.Strings.Returns)
        val wishlist = getString(component.localization.Strings.Wishlist)
        val profile = getString(component.localization.Strings.Profile)
        val logout = getString(component.localization.Strings.Logout)
    }

    enum class AccountMenuItem {
        ORDERS,
        RETURNS,
        WISHLIST,
        PROFILE,
        LOGOUT,
    }
}

fun DesktopNavContract.AccountMenuItem.label(): String {
    return when (this) {
        DesktopNavContract.AccountMenuItem.ORDERS -> DesktopNavContract.Strings.orders
        DesktopNavContract.AccountMenuItem.RETURNS -> DesktopNavContract.Strings.returns
        DesktopNavContract.AccountMenuItem.WISHLIST -> DesktopNavContract.Strings.wishlist
        DesktopNavContract.AccountMenuItem.PROFILE -> DesktopNavContract.Strings.profile
        DesktopNavContract.AccountMenuItem.LOGOUT -> DesktopNavContract.Strings.logout
    }
}

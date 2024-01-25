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
        val promoText = getString(component.localization.Strings.Navigation.PromoText)
        val helpAndFaq = getString(component.localization.Strings.Navigation.HelpAndFaq)
        val currencyEnUs = getString(component.localization.Strings.Navigation.CurrencyEnUs)
        val search = getString(component.localization.Strings.Navigation.Search)
        val login = getString(component.localization.Strings.Navigation.Login)
        val orders = getString(component.localization.Strings.Navigation.Orders)
        val returns = getString(component.localization.Strings.Navigation.Returns)
        val wishlist = getString(component.localization.Strings.Navigation.Wishlist)
        val profile = getString(component.localization.Strings.Navigation.Profile)
        val logout = getString(component.localization.Strings.Navigation.Logout)
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

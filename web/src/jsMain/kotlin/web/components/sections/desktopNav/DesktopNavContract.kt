package web.components.sections.desktopNav

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
        val promoText = "Free shipping on orders over £50"
        val helpAndFaq = "Help & FAQ"
        val currencyEnUs = "EN, $"
        val search = "Search"
        val login = "Login"
        val orders = "Orders"
        val returns = "Returns"
        val wishlist = "Wishlist"
        val account = "Account"
        val logout = "Log out"
    }

    enum class AccountMenuItem {
        ORDERS,
        RETURNS,
        WISHLIST,
        ACCOUNT,
        LOGOUT,
    }
}

fun DesktopNavContract.AccountMenuItem.label(): String {
    return when (this) {
        DesktopNavContract.AccountMenuItem.ORDERS -> DesktopNavContract.Strings.orders
        DesktopNavContract.AccountMenuItem.RETURNS -> DesktopNavContract.Strings.returns
        DesktopNavContract.AccountMenuItem.WISHLIST -> DesktopNavContract.Strings.wishlist
        DesktopNavContract.AccountMenuItem.ACCOUNT -> DesktopNavContract.Strings.account
        DesktopNavContract.AccountMenuItem.LOGOUT -> DesktopNavContract.Strings.logout
    }
}

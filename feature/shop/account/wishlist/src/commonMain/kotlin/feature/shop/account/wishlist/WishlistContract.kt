package feature.shop.account.wishlist

import component.localization.Strings
import component.localization.getString
import org.koin.core.component.KoinComponent

object WishlistContract : KoinComponent {
    data class State(
        val strings: Strings = Strings(
            signUp = getString(component.localization.Strings.SignUp),
        )
    )

    sealed interface Inputs {
        data object OnLogoutClicked : Inputs

        data class SetEmail(val email: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val signUp: String,
        val logout: String = getString(component.localization.Strings.Logout),
    ) {
    }
}

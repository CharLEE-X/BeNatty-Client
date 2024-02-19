package feature.shop.account.orders

import component.localization.getString

object OrdersContract {
    data class State(
        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class SetFullName(val fullName: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val firstName: String = getString(component.localization.Strings.FirstName),
    )
}

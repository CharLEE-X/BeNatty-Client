package feature.admin.order.list

import component.localization.getString

object AdminOrderListContract {
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
        val fullName: String = getString(component.localization.Strings.FullName),
    )
}

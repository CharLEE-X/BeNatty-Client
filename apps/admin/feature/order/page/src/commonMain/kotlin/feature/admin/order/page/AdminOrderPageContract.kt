package feature.admin.order.page

import component.localization.getString

object AdminOrderPageContract {
    data class State(
        val isLoading: Boolean = false,
    )

    sealed interface Inputs {
        data class SetFullName(val fullName: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

}

val adminOrderPageStrings: AdminOrderPageStrings = AdminOrderPageStrings()

data class AdminOrderPageStrings(
    val firstName: String = getString(component.localization.Strings.FirstName),
)

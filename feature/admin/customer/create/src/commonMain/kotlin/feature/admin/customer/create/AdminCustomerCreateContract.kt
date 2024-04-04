package feature.admin.customer.create

import component.localization.getString

object AdminCustomerCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val email: String = "",
        val emailError: String? = null,
        val emailShake: Boolean = false,
        val firstName: String = "",
        val lastName: String = "",
    )

    sealed interface Inputs {
        data object CreateCustomer : Inputs
        data class ShakeErrors(val email: Boolean) : Inputs

        data object OnCreateClick : Inputs
        data object OnGoBackClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetEmailError(val error: String) : Inputs
        data class SetEmailShake(val shake: Boolean) : Inputs
        data class SetDetailFirstName(val firstName: String) : Inputs
        data class SetDetailLastName(val lastName: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data class GoToCustomer(val id: String) : Events
    }
}

val adminCustomerCreateStrings = AdminCustomerCreateStrings

object AdminCustomerCreateStrings {
    val newCustomer: String = getString(component.localization.Strings.NewCustomer)
    val email: String = getString(component.localization.Strings.Email)
    val firstName: String = getString(component.localization.Strings.FirstName)
    val lastName: String = getString(component.localization.Strings.LastName)
    val create: String = getString(component.localization.Strings.Create)
}

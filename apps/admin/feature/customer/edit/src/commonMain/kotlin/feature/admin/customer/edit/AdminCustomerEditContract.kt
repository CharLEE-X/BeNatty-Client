package feature.admin.customer.edit

import data.GetCustomerByIdQuery
import data.type.Role

object AdminCustomerEditContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val original: GetCustomerByIdQuery.GetUserById = GetCustomerByIdQuery.GetUserById(
            id = "",
            details = GetCustomerByIdQuery.Details(
                email = "",
                firstName = "",
                language = "",
                lastName = "",
                phone = ""
            ),
            address = GetCustomerByIdQuery.Address(
                address = "",
                apartment = "",
                city = "",
                company = "",
                country = "",
                firstName = "",
                lastName = "",
                phone = "",
                postcode = ""
            ),
            collectTax = true,
            emailVerified = false,
            marketingEmails = false,
            marketingSms = false,
            role = Role.Guest,
            wishlist = listOf(),
            updatedAt = "",
        ),
        val current: GetCustomerByIdQuery.GetUserById = original,

        val emailError: String? = "",
        val emailShake: Boolean = false,
    )

    sealed interface Inputs {
        data class Init(val customerId: String) : Inputs
        data class GetCustomerById(val customerId: String) : Inputs
        data object UpdateUser : Inputs
        data class ShakeErrors(val email: Boolean) : Inputs

        data object OnSaveClick : Inputs
        data object OnDiscardClick : Inputs
        data object OnGoBackClick : Inputs
        data object OnWarningYesClick : Inputs
        data object OnDeleteClick : Inputs

        data class SetOriginal(val customer: GetCustomerByIdQuery.GetUserById) : Inputs
        data class SetCurrent(val customer: GetCustomerByIdQuery.GetUserById) : Inputs
        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetEmailError(val error: String) : Inputs
        data class SetEmailShake(val shake: Boolean) : Inputs
        data class SetDetailFirstName(val firstName: String) : Inputs
        data class SetDetailLastName(val lastName: String) : Inputs
        data class SetLanguage(val language: String) : Inputs
        data class SetDetailPhone(val phone: String) : Inputs
        data class SetCountry(val country: String) : Inputs
        data class SetAddressFirstName(val firstName: String) : Inputs
        data class SetAddressLastName(val lastName: String) : Inputs
        data class SetCompany(val company: String) : Inputs
        data class SetAddress(val address: String) : Inputs
        data class SetApartment(val apartment: String) : Inputs
        data class SetPostcode(val postcode: String) : Inputs
        data class SetCity(val city: String) : Inputs
        data class SetAddressPhone(val phone: String) : Inputs
        data class SetCollectTax(val collectTax: Boolean) : Inputs
        data class SetMarketingEmail(val marketingEmail: Boolean) : Inputs
        data class SetMarketingSMS(val marketingSms: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data object ShowLeavePageWarningDialog : Events
        data class GoToCustomer(val id: String) : Events
    }
}

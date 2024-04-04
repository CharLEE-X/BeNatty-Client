package feature.admin.customer.page

import component.localization.getString
import core.models.PageScreenState
import data.GetCustomerByIdQuery
import data.type.Role

object AdminCustomerPageContract {
    data class State(
        val isLoading: Boolean = false,
        val pageScreenState: PageScreenState = PageScreenState.New,
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
        data class Init(val userId: String?) : Inputs
        data class GetUserById(val userId: String) : Inputs

        data object OnSaveClick : Inputs
        data object OnDiscardClick : Inputs
        data object OnGoBackClick : Inputs
        data object OnWarningYesClick : Inputs
        data object OnDeleteClick : Inputs

        data class SetOriginal(val user: GetCustomerByIdQuery.GetUserById) : Inputs
        data class SetCurrent(val user: GetCustomerByIdQuery.GetUserById) : Inputs
        data class SetPageScreenState(val pageScreenState: PageScreenState) : Inputs
        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetEmailError(val error: String) : Inputs
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
        data class GoToUser(val id: String) : Events
    }
}

val adminCustomerPageStrings: AdminCustomerPageStrings = AdminCustomerPageStrings()

data class AdminCustomerPageStrings(
    val save: String = getString(component.localization.Strings.Save),
    val discard: String = getString(component.localization.Strings.Discard),
    val delete: String = getString(component.localization.Strings.Delete),

    val email: String = getString(component.localization.Strings.Email),
    val firstName: String = getString(component.localization.Strings.FirstName),
    val lastName: String = getString(component.localization.Strings.LastName),
    val language: String = getString(component.localization.Strings.Language),
    val phone: String = getString(component.localization.Strings.Phone),

    val country: String = getString(component.localization.Strings.Country),
    val company: String = getString(component.localization.Strings.Company),
    val address: String = getString(component.localization.Strings.Address),
    val addressDesc: String = getString(component.localization.Strings.AddressDesc),
    val apartment: String = getString(component.localization.Strings.Apartment),
    val postcode: String = getString(component.localization.Strings.PostCode),
    val city: String = getString(component.localization.Strings.City),

    val collectTax: String = getString(component.localization.Strings.CollectTax),
    val marketingEmailsAgreed: String = getString(component.localization.Strings.MarketingEmailsAgreed),
    val marketingSMSAgreed: String = getString(component.localization.Strings.MarketingSMSAgreed),

    val newCustomer: String = getString(component.localization.Strings.NewCustomer),
    val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
    val saveChanges: String = getString(component.localization.Strings.SaveChanges),
    val dismiss: String = getString(component.localization.Strings.Dismiss),
    val user: String = getString(component.localization.Strings.User),
    val id: String = getString(component.localization.Strings.Id),
    val deleteExplain: String = getString(component.localization.Strings.DeleteExplain),
    val discardAllUnsavedChanges: String = getString(component.localization.Strings.DiscardAllUnsavedChanges),
    val discardAllUnsavedChangesDesc: String = getString(component.localization.Strings.DiscardAllUnsavedChangesDesc),
    val continueEditing: String = getString(component.localization.Strings.ContinueEditing),
    val discardChanges: String = getString(component.localization.Strings.DiscardChanges),
    val languageDesc: String = getString(component.localization.Strings.LanguageDesc),
    val marketingDesc: String = getString(component.localization.Strings.MarketingDesc),
    val leavePageWithUnsavedChanges: String = getString(component.localization.Strings.LeavePageWithUnsavedChanges),
    val leavingThisPageWillDiscardAllUnsavedChanges: String =
        getString(component.localization.Strings.LeavingThisPageWillDiscardAllUnsavedChanges),
    val leavePage: String = getString(component.localization.Strings.LeavePage),
    val stay: String = getString(component.localization.Strings.Stay),
)

package feature.admin.user.create

import component.localization.getString

object AdminCustomerCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val wasEdited: Boolean = false,

        val email: String = "",
        val emailError: String? = null,
        val emailShake: Boolean = false,
        val detailFirstName: String = "",
        val detailLastName: String = "",
        val language: String = "",
        val detailPhone: String = "",

        val country: String = "",
        val addressFirstName: String = "",
        val addressLastName: String = "",
        val company: String = "",
        val address: String = "",
        val apartment: String = "",
        val postcode: String = "",
        val city: String = "",
        val addressPhone: String = "",

        val collectTax: Boolean = true,
        val marketingEmails: Boolean = false,
        val marketingSms: Boolean = false,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        sealed interface OnClick : Inputs {
            data object Save : Inputs
            data object Discard : Inputs
            data object GoBack : Inputs
            data object WarningYes : Inputs
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Inputs

            data class Email(val email: String) : Inputs
            data class EmailError(val error: String) : Inputs
            data class DetailFirstName(val firstName: String) : Inputs
            data class DetailLastName(val lastName: String) : Inputs
            data class Language(val language: String) : Inputs
            data class DetailPhone(val phone: String) : Inputs

            data class Country(val country: String) : Inputs
            data class AddressFirstName(val firstName: String) : Inputs
            data class AddressLastName(val lastName: String) : Inputs
            data class Company(val company: String) : Inputs
            data class Address(val address: String) : Inputs
            data class Apartment(val apartment: String) : Inputs
            data class Postcode(val postcode: String) : Inputs
            data class City(val city: String) : Inputs
            data class AddressPhone(val phone: String) : Inputs

            data class CollectTax(val collectTax: Boolean) : Inputs
            data class MarketingEmail(val marketingEmail: Boolean) : Inputs
            data class MarketingSMS(val marketingSms: Boolean) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data object ShowLeavePageWarningDialog : Events
        data class GoToUser(val id: String) : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val discard: String = getString(component.localization.Strings.Discard),

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
    ) {
    }
}

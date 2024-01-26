package feature.account.profile

import component.localization.getString
import data.GetUserProfileQuery
import org.koin.core.component.KoinComponent

object ProfileContract : KoinComponent {
    data class State(
        val originalUser: GetUserProfileQuery.GetUser = GetUserProfileQuery.GetUser(
            id = "",
            email = "",
            details = GetUserProfileQuery.Details(
                name = "",
                phone = "",
            ),
            address = GetUserProfileQuery.Address(
                address = "",
                additionalInfo = "",
                postcode = "",
                city = "",
                state = "",
                country = "",
            ),
            __typename = "",
        ),
        val fullName: String = "",
        val fullNameError: String? = null,
        val email: String = "",
        val emailError: String? = null,
        val phone: String = "",
        val phoneError: String? = null,
        val isSavePersonalDetailsButtonDisabled: Boolean = true,

        val oldPassword: String = "",
        val oldPasswordError: String? = null,
        val newPassword: String = "",
        val newPasswordError: String? = null,
        val isSavePasswordButtonDisabled: Boolean = true,

        val address: String = "",
        val addressError: String? = null,
        val additionalInformation: String = "",
        val additionalInformationError: String? = null,
        val postcode: String = "",
        val postcodeError: String? = null,
        val city: String = "",
        val cityError: String? = null,
        val state: String = "",
        val stateError: String? = null,
        val country: String = "",
        val countryError: String? = null,
        val isSaveAddressButtonDisabled: Boolean = true,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data object GetUserProfile : Inputs
        data class SetUserProfile(val user: GetUserProfileQuery.GetUser) : Inputs

        data class SetFullName(val fullName: String) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetPhone(val phone: String) : Inputs
        data object SavePersonalDetails : Inputs
        data class SetPersonalDetailsButton(val isDisabled: Boolean) : Inputs

        data class SetOldPassword(val oldPassword: String) : Inputs
        data class SetNewPassword(val newPassword: String) : Inputs
        data object SavePassword : Inputs
        data class SetPasswordButton(val isDisabled: Boolean) : Inputs

        data class SetAddress(val address: String) : Inputs
        data class SetAdditionalInformation(val additionalInformation: String) : Inputs
        data class SetPostcode(val postcode: String) : Inputs
        data class SetCity(val city: String) : Inputs
        data class SetState(val state: String) : Inputs
        data class SetCountry(val country: String) : Inputs
        data object SaveAddress : Inputs
        data class SetAddressButton(val isDisabled: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val fullName: String = getString(component.localization.Strings.FullName),
        val email: String = getString(component.localization.Strings.Email),
        val phone: String = getString(component.localization.Strings.Phone),
        val save: String = getString(component.localization.Strings.Save),
        val personalDetails: String = getString(component.localization.Strings.PersonalDetails),
        val profile: String = getString(component.localization.Strings.Profile),
        val oldPassword: String = getString(component.localization.Strings.OldPassword),
        val newPassword: String = getString(component.localization.Strings.NewPassword),
        val address: String = getString(component.localization.Strings.Address),
        val additionalInformation: String = getString(component.localization.Strings.AdditionalInformation),
        val postcode: String = getString(component.localization.Strings.PostalCode),
        val city: String = getString(component.localization.Strings.City),
        val state: String = getString(component.localization.Strings.State),
        val country: String = getString(component.localization.Strings.Country),
    )
}

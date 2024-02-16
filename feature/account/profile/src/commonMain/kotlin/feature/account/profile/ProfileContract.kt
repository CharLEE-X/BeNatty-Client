package feature.account.profile

import component.localization.getString
import data.GetUserByIdQuery
import data.type.Role

object ProfileContract {
    data class State(
        // Personal details
        val detailsFirstName: String = "",
        val fullNameError: String? = null,
        val shakeFullName: Boolean = false,

        val email: String = "",
        val emailError: String? = null,
        val shakeEmail: Boolean = false,

        val phone: String = "",
        val phoneError: String? = null,
        val shakePhone: Boolean = false,

        val isPersonalDetailsEditing: Boolean = false,
        val isSavePersonalDetailsButtonDisabled: Boolean = true,

        // Password
        val oldPassword: String = "",
        val oldPasswordError: String? = null,
        val shakeOldPassword: Boolean = false,

        val newPassword: String = "",
        val newPasswordError: String? = null,
        val shakeNewPassword: Boolean = false,

        val isSavePasswordButtonDisabled: Boolean = true,

        // Address
        val address: String = "",
        val addressError: String? = null,
        val shakeAddress: Boolean = false,

        val additionalInformation: String = "",
        val additionalInformationError: String? = null,

        val postcode: String = "",
        val postcodeError: String? = null,
        val shakePostcode: Boolean = false,

        val city: String = "",
        val cityError: String? = null,
        val shakeCity: Boolean = false,

        val state: String = "",
        val stateError: String? = null,
        val shakeState: Boolean = false,

        val country: String = "",
        val countryError: String? = null,
        val shakeCountry: Boolean = false,

        val isAddressEditing: Boolean = false,
        val isSaveAddressButtonDisabled: Boolean = true,

        val original: GetUserByIdQuery.GetUserById = GetUserByIdQuery.GetUserById(
            user = GetUserByIdQuery.User(
                id = "",
                details = GetUserByIdQuery.Details(
                    email = email,
                    firstName = detailsFirstName,
                    lastName = "lastName",
                    language = "language",
                    phone = phone,
                ),
                address = GetUserByIdQuery.Address(
                    address = address,
                    postcode = postcode,
                    city = city,
                    country = country,
                    apartment = "apartment",
                    company = "company",
                    firstName = "firstName",
                    lastName = "lastName", phone = null,

                    ),
                createdAt = "createdAt",
                collectTax = true,
                marketingEmails = true,
                marketingSms = true,
                emailVerified = true,
                updatedAt = "updatedAt",
                wishlist = emptyList(),
                role = Role.User,
                __typename = "__typename",
            ),
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data object GetUserProfile : Inputs
        data class SetUserProfile(val user: GetUserByIdQuery.GetUserById) : Inputs

        data class SetDetailsFullName(val fullName: String) : Inputs
        data class SetFullNameShake(val shake: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetEmailShake(val shake: Boolean) : Inputs
        data class SetPhone(val phone: String) : Inputs
        data class SetPhoneShake(val shake: Boolean) : Inputs
        data object SavePersonalDetails : Inputs
        data class SetPersonalDetailsButtonDisabled(val isDisabled: Boolean) : Inputs
        data object SetPersonalDetailsEditable : Inputs
        data object SetPersonalDetailsNotEditable : Inputs

        data class SetOldPassword(val oldPassword: String) : Inputs
        data class SetOldPasswordShake(val shake: Boolean) : Inputs
        data class SetNewPassword(val newPassword: String) : Inputs
        data class SetNewPasswordShake(val shake: Boolean) : Inputs
        data object SavePassword : Inputs
        data class SetPasswordButtonDisabled(val isDisabled: Boolean) : Inputs

        data class SetAddress(val address: String) : Inputs
        data class SetAddressShake(val shake: Boolean) : Inputs
        data class SetAdditionalInformation(val additionalInformation: String) : Inputs
        data class SetPostcode(val postcode: String) : Inputs
        data class SetPostcodeShake(val shake: Boolean) : Inputs
        data class SetCity(val city: String) : Inputs
        data class SetCityShake(val shake: Boolean) : Inputs
        data class SetState(val state: String) : Inputs
        data class SetStateShake(val shake: Boolean) : Inputs
        data class SetCountry(val country: String) : Inputs
        data class SetCountryShake(val shake: Boolean) : Inputs
        data object SaveAddress : Inputs
        data class SetAddressButtonDisabled(val isDisabled: Boolean) : Inputs
        data object SetAddressEditable : Inputs
        data object SetAddressNotEditable : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val edit: String = getString(component.localization.Strings.Edit),
        val discard: String = getString(component.localization.Strings.Discard),
        val firstName: String = getString(component.localization.Strings.FirstName),
        val email: String = getString(component.localization.Strings.Email),
        val phone: String = getString(component.localization.Strings.Phone),
        val personalDetails: String = getString(component.localization.Strings.PersonalDetails),
        val profile: String = getString(component.localization.Strings.Profile),
        val oldPassword: String = getString(component.localization.Strings.OldPassword),
        val newPassword: String = getString(component.localization.Strings.NewPassword),
        val address: String = getString(component.localization.Strings.Address),
        val company: String = getString(component.localization.Strings.Company),
        val postcode: String = getString(component.localization.Strings.PostCode),
        val city: String = getString(component.localization.Strings.City),
        val apartment: String = getString(component.localization.Strings.Apartment),
        val country: String = getString(component.localization.Strings.Country),
        val password: String = getString(component.localization.Strings.Password),
    )
}

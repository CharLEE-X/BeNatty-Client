package feature.admin.user.page

import component.localization.getString
import data.UserGetByIdQuery
import data.type.Role

object AdminUserPageContract {
    data class State(
        val isLoading: Boolean = false,
        val originalUser: UserGetByIdQuery.GetUserById = UserGetByIdQuery.GetUserById(
            id = "",
            email = "",
            role = Role.USER,
            details = UserGetByIdQuery.Details(
                name = "",
                phone = "",
            ),
            address = UserGetByIdQuery.Address(
                address = "",
                additionalInfo = "",
                postcode = "",
                city = "",
                state = "",
                country = "",
            ),
            __typename = "",
        ),

        val screenState: ScreenState = ScreenState.New.Create,

        // Personal details
        val id: String? = null,

        val fullName: String = "",
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

        // Role
        val role: Role = Role.USER,
        val isSaveRoleButtonDisabled: Boolean = true,

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

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs

        data object CreateNewUser : Inputs
        data class GetUserById(val id: String) : Inputs

        data class SetId(val id: String) : Inputs
        data class SetUserProfile(val user: UserGetByIdQuery.GetUserById) : Inputs

        data object DeleteUser : Inputs

        data class SetFullName(val fullName: String) : Inputs
        data class SetFullNameShake(val shake: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetEmailShake(val shake: Boolean) : Inputs
        data class SetPhone(val phone: String) : Inputs
        data class SetPhoneShake(val shake: Boolean) : Inputs
        data object SavePersonalDetails : Inputs
        data object SetPersonalDetailsEditable : Inputs
        data object SetPersonalDetailsNotEditable : Inputs
        data class SetPersonalDetailsButtonDisabled(val isDisabled: Boolean) : Inputs

        data object ResetPassword : Inputs

        data class SetRole(val role: Role) : Inputs
        data class SetRoleButtonDisabled(val isDisabled: Boolean) : Inputs
        data object SaveRole : Inputs

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
        data object SetAddressEditable : Inputs
        data object SetAddressNotEditable : Inputs
        data class SetAddressButtonDisabled(val isDisabled: Boolean) : Inputs

    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToUserList : Events
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
        val edit: String = getString(component.localization.Strings.Edit),
        val cancel: String = getString(component.localization.Strings.Cancel),
        val delete: String = getString(component.localization.Strings.Delete),
        val password: String = getString(component.localization.Strings.Password),
        val resetPassword: String = getString(component.localization.Strings.ResetPassword),
        val role: String = getString(component.localization.Strings.Role),
    )

    sealed interface ScreenState {
        sealed interface New : ScreenState {
            data object Create : New
            data object Created : New
        }

        sealed interface Existing : ScreenState {
            data object Read : Existing
            data object Edit : Existing
        }
    }
}

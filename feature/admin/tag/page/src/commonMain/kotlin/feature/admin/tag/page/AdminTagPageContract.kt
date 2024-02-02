package feature.admin.tag.page

import com.apollographql.apollo3.mpp.currentTimeMillis
import component.localization.getString
import data.UserGetByIdQuery
import data.type.Role

object AdminTagPageContract {
    data class State(
        val isLoading: Boolean = false,
        val original: UserGetByIdQuery.GetUserById = UserGetByIdQuery.GetUserById(
            id = "",
            email = "",
            role = Role.USER,
            emailVerified = false,
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
            wishlist = emptyList(),
            lastActive = null,
            createdBy = null,
            createdAt = "",
            updatedAt = "",
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

        val emailVerified: Boolean = false,

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

        val wishlistSize: Int = 0,
        val lastActive: String? = null,
        val createdBy: String? = null,
        val createdAt: Long = currentTimeMillis(),
        val updatedAt: Long = currentTimeMillis(),

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
        data class SetIsEmailVerified(val isVerified: Boolean) : Inputs
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

        data class SetWishlistSize(val size: Int) : Inputs
        data class SetLastActive(val lastActive: String?) : Inputs
        data class SetCreatedBy(val createdBy: String?) : Inputs
        data class SetCreatedAt(val createdAt: Long) : Inputs
        data class SetUpdatedAt(val updatedAt: Long) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToUserList : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val edit: String = getString(component.localization.Strings.Edit),
        val cancel: String = getString(component.localization.Strings.Cancel),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val updatedAt: String = getString(component.localization.Strings.UpdatedAt),
        val never: String = getString(component.localization.Strings.Never),
        val fullName: String = getString(component.localization.Strings.FullName),
        val email: String = getString(component.localization.Strings.Email),
        val phone: String = getString(component.localization.Strings.Phone),
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
        val password: String = getString(component.localization.Strings.Password),
        val resetPassword: String = getString(component.localization.Strings.ResetPassword),
        val role: String = getString(component.localization.Strings.Role),
        val verified: String = getString(component.localization.Strings.Verified),
        val notVerified: String = getString(component.localization.Strings.NotVerified),
        val otherInfo: String = getString(component.localization.Strings.OtherInfo),
        val wishlist: String = getString(component.localization.Strings.Wishlist),
        val lastActive: String = getString(component.localization.Strings.LastActive),
        val registered: String = getString(component.localization.Strings.Registered),
        val createUser: String = getString(component.localization.Strings.CreateUser),
        val user: String = getString(component.localization.Strings.User),
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

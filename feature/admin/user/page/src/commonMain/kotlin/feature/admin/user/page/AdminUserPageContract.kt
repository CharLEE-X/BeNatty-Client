package feature.admin.user.page

import com.apollographql.apollo3.mpp.currentTimeMillis
import component.localization.getString
import data.UserGetByIdQuery
import data.type.Role

object AdminUserPageContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,
        val screenState: ScreenState = ScreenState.New,

        val original: UserGetByIdQuery.GetUserById = UserGetByIdQuery.GetUserById(
            id = "",
            email = "",
            role = Role.User,
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
        val current: UserGetByIdQuery.GetUserById = original,

        // Personal details
        val nameError: String? = null,
        val shakeFullName: Boolean = false,
        val emailError: String? = null,
        val shakeEmail: Boolean = false,
        val emailVerified: Boolean = false,
        val phoneError: String? = null,
        val shakePhone: Boolean = false,
        val addressError: String? = null,
        val shakeAddress: Boolean = false,
        val additionalInformationError: String? = null,
        val postcodeError: String? = null,
        val shakePostcode: Boolean = false,
        val cityError: String? = null,
        val shakeCity: Boolean = false,
        val stateError: String? = null,
        val shakeState: Boolean = false,
        val countryError: String? = null,
        val shakeCountry: Boolean = false,
        val lastActive: String? = null,
        val createdBy: String? = null,
        val createdAt: Long = currentTimeMillis(),
        val updatedAt: Long = currentTimeMillis(),
        val wishlistSize: Int = 0,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        sealed interface Get : Inputs {
            data class UserById(val id: String) : Inputs
        }

        sealed interface OnCLick : Inputs {
            data object Create : Inputs
            data object Delete : Inputs
            data object ResetPassword : Inputs
            data object SaveEdit : Inputs
            data object CancelEdit : Inputs
        }

        sealed interface Set : Inputs {
            data class OriginalUser(val user: UserGetByIdQuery.GetUserById) : Inputs
            data class CurrentUser(val user: UserGetByIdQuery.GetUserById) : Inputs
            data class Loading(val isLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs

            data class Id(val id: String) : Inputs
            data class FullName(val fullName: String) : Inputs
            data class NameShake(val shake: Boolean) : Inputs
            data class Email(val email: String) : Inputs
            data class IsEmailVerified(val isVerified: Boolean) : Inputs
            data class EmailShake(val shake: Boolean) : Inputs
            data class Phone(val phone: String) : Inputs
            data class PhoneShake(val shake: Boolean) : Inputs
            data class UserRole(val role: Role) : Inputs
            data class Address(val address: String) : Inputs
            data class AddressShake(val shake: Boolean) : Inputs
            data class AdditionalInformation(val additionalInformation: String) : Inputs
            data class Postcode(val postcode: String) : Inputs
            data class PostcodeShake(val shake: Boolean) : Inputs
            data class City(val city: String) : Inputs
            data class CityShake(val shake: Boolean) : Inputs
            data class State(val state: String) : Inputs
            data class StateShake(val shake: Boolean) : Inputs
            data class Country(val country: String) : Inputs
            data class CountryShake(val shake: Boolean) : Inputs
            data class WishlistSize(val size: Int) : Inputs
            data class LastActive(val lastActive: String?) : Inputs
            data class CreatedBy(val createdBy: String?) : Inputs
            data class CreatedAt(val createdAt: Long) : Inputs
            data class UpdatedAt(val updatedAt: Long) : Inputs
        }
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
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
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
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val reset: String = getString(component.localization.Strings.Reset),
        val user: String = getString(component.localization.Strings.User),
        val id: String = getString(component.localization.Strings.Id),
    )

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}

package feature.admin.user.create

import com.apollographql.apollo3.mpp.currentTimeMillis
import component.localization.getString
import data.UserGetByIdQuery

object AdminUserCreateContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,
        val screenState: ScreenState = ScreenState.New,

        val original: UserGetByIdQuery.GetUserById = UserGetByIdQuery.GetUserById(
            user = UserGetByIdQuery.User(
                id = "",
                details = UserGetByIdQuery.Details(
                    email = "",
                    firstName = "",
                    lastName = "",
                    language = "",
                    phone = "",
                ),
                address = UserGetByIdQuery.Address(
                    country = "",
                    firstName = "",
                    lastName = "",
                    company = "",
                    address = "",
                    apartment = "",
                    postcode = "",
                    city = "",
                ),
                collectTax = true,
                marketingEmails = false,
                marketingSms = false,
                emailVerified = false,
                wishlist = emptyList(),
                createdAt = "",
                updatedAt = "",
                __typename = "",
            ),
        ),
        val current: UserGetByIdQuery.GetUserById = original,

        val emailError: String? = null,
        val emailShake: Boolean = false,

        val createdBy: String? = null,
        val createdAt: Long = currentTimeMillis(),
        val updatedAt: Long = currentTimeMillis(),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        sealed interface Get : Inputs {
            data class UserById(val id: String) : Inputs
        }

        sealed interface OnClick : Inputs {
            data object Create : Inputs
            data object Delete : Inputs
            data object Save : Inputs
            data object Discard : Inputs
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Inputs

            data class OriginalUser(val user: UserGetByIdQuery.GetUserById) : Inputs
            data class CurrentUser(val user: UserGetByIdQuery.GetUserById) : Inputs

            data class Id(val id: String) : Inputs
            data class Email(val email: String) : Inputs
            data class DetailFirstName(val firstName: String) : Inputs
            data class DetailLastName(val lastName: String) : Inputs
            data class Language(val language: String) : Inputs
            data class Phone(val phone: String) : Inputs

            data class Country(val country: String) : Inputs
            data class AddressFirstName(val firstName: String) : Inputs
            data class AddressLastName(val lastName: String) : Inputs
            data class Company(val company: String) : Inputs
            data class Address(val address: String) : Inputs
            data class Apartment(val apartment: String) : Inputs
            data class City(val city: String) : Inputs
            data class Postcode(val postcode: String) : Inputs

            data class CollectTax(val collectTax: String) : Inputs
            data class MarketingEmail(val marketingEmail: String) : Inputs
            data class MarketingSMS(val marketingSms: String) : Inputs

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
        val newCustomer: String = getString(component.localization.Strings.NewCustomer),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val dismiss: String = getString(component.localization.Strings.Dismiss),
        val user: String = getString(component.localization.Strings.User),
        val id: String = getString(component.localization.Strings.Id),
        val deleteExplain: String = getString(component.localization.Strings.DeleteExplain),
        val addressDesc: String = getString(component.localization.Strings.AddressDesc),
        val collectTax: String = getString(component.localization.Strings.CollectTax),
        val marketingEmailsAgreed: String = getString(component.localization.Strings.MarketingEmailsAgreed),
        val marketingSMSAgreed: String = getString(component.localization.Strings.MarketingSMSAgreed),
    ) {
    }

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}

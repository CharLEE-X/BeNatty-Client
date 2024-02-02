package feature.admin.category.page

import com.apollographql.apollo3.mpp.currentTimeMillis
import component.localization.getString
import data.GetCategoryByIdQuery
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AdminCategoryPageContract : KoinComponent {
    private val authService: AuthService by inject()

    data class State(
        val isLoading: Boolean = false,
        val screenState: ScreenState = ScreenState.New.Create,

        // Personal details
        val id: String? = null,

        val name: String = "",
        val nameError: String? = null,
        val shakeName: Boolean = false,

        val description: String = "",
        val parentId: String? = null,
        val display: Boolean = true,
        val createdBy: String = authService.userId ?: "",
        val createdAt: Long = currentTimeMillis(),
        val updatedAt: Long = currentTimeMillis(),

        val isDetailsEditing: Boolean = false,
        val isSaveButtonDisabled: Boolean = true,

        val isCreateButtonDisabled: Boolean = true,

        val original: GetCategoryByIdQuery.GetCategoryById = GetCategoryByIdQuery.GetCategoryById(
            id = id ?: "",
            name = name,
            description = description,
            parentId = parentId,
            display = true,
            createdBy = null,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs

        data object CreateNew : Inputs
        data class GetById(val id: String) : Inputs
        data object Delete : Inputs

        data class SetCategoryProfile(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs
        data class SetId(val id: String) : Inputs

        data class SetName(val fullName: String) : Inputs
        data class SetNameShake(val shake: Boolean) : Inputs
        data class SetDetailsEditable(val isEditable: Boolean) : Inputs
        data class SetSaveButtonDisabled(val isDisabled: Boolean) : Inputs
        data object SaveDetails : Inputs

        data class SetCreatedBy(val createdBy: String) : Inputs
        data class SetCreatedAt(val createdAt: Long) : Inputs
        data class SetUpdatedAt(val updatedAt: Long) : Inputs

        data class SetDescription(val description: String) : Inputs
        data class SetParentId(val parentId: String) : Inputs
        data class SetDisplay(val display: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToList : Events
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
        val createCategory: String = getString(component.localization.Strings.CreateCategory),
        val category: String = getString(component.localization.Strings.Category),
        val details: String = getString(component.localization.Strings.Details),
        val name: String = getString(component.localization.Strings.Name),
        val create: String = getString(component.localization.Strings.Create),
        val description: String = getString(component.localization.Strings.Description),
        val display: String = getString(component.localization.Strings.Display),
        val parentId: String = getString(component.localization.Strings.ParentId),
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

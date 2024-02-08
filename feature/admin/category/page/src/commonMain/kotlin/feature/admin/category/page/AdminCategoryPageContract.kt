package feature.admin.category.page

import component.localization.getString
import data.GetCategoriesAllMinimalQuery
import data.GetCategoryByIdQuery
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AdminCategoryPageContract : KoinComponent {
    private val authService: AuthService by inject()

    data class State(
        val isLoading: Boolean = false,
        val screenState: ScreenState = ScreenState.Existing.Read,

        val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal> = emptyList(),

        // Personal details
        val id: String? = null,

        val name: String = "",
        val nameError: String? = null,
        val shakeName: Boolean = false,

        val description: String = "",
        val parent: GetCategoryByIdQuery.Parent? = null,
        val display: Boolean = true,
        val creator: GetCategoryByIdQuery.Creator = GetCategoryByIdQuery.Creator(
            id = authService.userId ?: "",
            name = "",
        ),
        val createdAt: String = "",
        val updatedAt: String = "",

        val wasEdited: Boolean = false,
        val isSaveButtonDisabled: Boolean = true,

        val isCreateButtonDisabled: Boolean = true,

        val original: GetCategoryByIdQuery.GetCategoryById = GetCategoryByIdQuery.GetCategoryById(
            id = id ?: "",
            name = name,
            description = description,
            parent = null,
            display = true,
            creator = GetCategoryByIdQuery.Creator(
                id = authService.userId ?: "",
                name = "",
            ),
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        sealed interface Get : Inputs {
            data class CategoryById(val id: String) : Inputs
            data object AllCategories : Inputs
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs

            data class AllCategories(val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal>) :
                Inputs

            data class OriginalCategory(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs
            data class Id(val id: String) : Inputs

            data class Name(val fullName: String) : Inputs
            data class IsNameShake(val shake: Boolean) : Inputs
            data class IsDetailsEditable(val isEditable: Boolean) : Inputs
            data class IsSaveButtonDisabled(val isDisabled: Boolean) : Inputs

            data class Description(val description: String) : Inputs
            data class Parent(val parent: GetCategoryByIdQuery.Parent?) : Inputs
            data class Display(val display: Boolean) : Inputs

            data class Creator(val creator: GetCategoryByIdQuery.Creator) : Inputs
            data class CreatedAt(val createdAt: String) : Inputs
            data class UpdatedAt(val updatedAt: String) : Inputs
        }

        sealed interface OnClick : Inputs {
            data object CreateNew : Inputs
            data object Delete : Inputs
            data object Edit : Inputs
            data object Cancel : Inputs
            data object SaveDetails : Inputs
            data object Parent : Inputs
            data object Creator : Inputs
            data class ParentPicker(val category: GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToList : Events
        data class GoToUser(val id: String) : Events
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
        val parentCategory: String = getString(component.localization.Strings.ParentCategory),
        val none: String = getString(component.localization.Strings.None),
        val noOtherCategoriesToChooseFrom: String = getString(component.localization.Strings.NoOtherCategoriesToChooseFrom),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val reset: String = getString(component.localization.Strings.Reset),
    )

    sealed interface ScreenState {
        data object Create : ScreenState

        sealed interface Existing : ScreenState {
            data object Read : Existing
            data object Edit : Existing
        }
    }
}

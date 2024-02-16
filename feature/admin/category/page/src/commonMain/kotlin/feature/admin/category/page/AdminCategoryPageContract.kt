package feature.admin.category.page

import component.localization.getString
import data.GetCategoriesAllMinimalQuery
import data.GetCategoryByIdQuery

object AdminCategoryPageContract {
    data class State(
        val isLoading: Boolean = false,
        val screenState: ScreenState,

        val wasEdited: Boolean = false,

        val allCategories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal> = emptyList(),

        val nameError: String? = null,
        val shakeName: Boolean = false,
        val displayError: String? = null,
        val shakeDisplay: Boolean = false,
        val weightError: String? = null,
        val shakeWeight: Boolean = false,
        val lengthError: String? = null,
        val shakeLength: Boolean = false,
        val widthError: String? = null,
        val shakeWidth: Boolean = false,
        val heightError: String? = null,
        val shakeHeight: Boolean = false,

        val original: GetCategoryByIdQuery.GetCategoryById = GetCategoryByIdQuery.GetCategoryById(
            id = "",
            name = "",
            description = "",
            parent = null,
            display = false,
            shippingPreset = GetCategoryByIdQuery.ShippingPreset(
                weight = "",
                length = "",
                width = "",
                height = "",
                isPhysicalProduct = true,
            ),
            creator = GetCategoryByIdQuery.Creator(
                id = "",
                firstName = "",
                lastName = "",
            ),
            createdAt = "",
            updatedAt = "",
        ),

        val current: GetCategoryByIdQuery.GetCategoryById = original,
        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        sealed interface Get : Inputs {
            data class CategoryById(val id: String) : Inputs
            data class AllCategories(val currentCategoryId: String?) : Inputs
        }

        sealed interface OnClick : Inputs {
            data object Create : Inputs
            data object Delete : Inputs
            data object SaveEdit : Inputs
            data object CancelEdit : Inputs
            data object GoToParent : Inputs
            data object GoToUserCreator : Inputs
            data class ParentCategorySelected(val categoryName: String) : Inputs
            data object GoToCreateCategory : Inputs
            data object ImproveDescription : Inputs
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs

            data class AllCategories(val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal>) :
                Inputs

            data class OriginalCategory(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs
            data class CurrentCategory(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs

            data class Id(val id: String) : Inputs
            data class Name(val fullName: String) : Inputs
            data class IsNameShake(val shake: Boolean) : Inputs
            data class Description(val description: String) : Inputs
            data class Parent(val parent: GetCategoryByIdQuery.Parent?) : Inputs
            data class Display(val display: Boolean) : Inputs
            data class IsDisplayShake(val shake: Boolean) : Inputs
            data class Creator(val creator: GetCategoryByIdQuery.Creator) : Inputs
            data class CreatedAt(val createdAt: String) : Inputs
            data class UpdatedAt(val updatedAt: String) : Inputs
            data class Weight(val weight: String) : Inputs
            data class IsWeightShake(val shake: Boolean) : Inputs
            data class Length(val length: String) : Inputs
            data class IsLengthShake(val shake: Boolean) : Inputs
            data class Width(val width: String) : Inputs
            data class IsWidthShake(val shake: Boolean) : Inputs
            data class Height(val height: String) : Inputs
            data class IsHeightShake(val shake: Boolean) : Inputs
            data class RequiresShipping(val requiresShipping: Boolean) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToList : Events
        data class GoToUser(val id: String) : Events
        data object GoToCreateCategory : Events
        data class GoToCategory(val id: String) : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val edit: String = getString(component.localization.Strings.Edit),
        val discard: String = getString(component.localization.Strings.Discard),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
        val createCategory: String = getString(component.localization.Strings.CreateCategory),
        val category: String = getString(component.localization.Strings.Category),
        val details: String = getString(component.localization.Strings.Details),
        val name: String = getString(component.localization.Strings.Name),
        val create: String = getString(component.localization.Strings.Create),
        val description: String = getString(component.localization.Strings.Description),
        val display: String = getString(component.localization.Strings.Display),
        val parentCategory: String = getString(component.localization.Strings.ParentCategory),
        val none: String = getString(component.localization.Strings.None),
        val noCategories: String = getString(component.localization.Strings.NoCategories),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val dismiss: String = getString(component.localization.Strings.Dismiss),
        val shippingPreset: String = getString(component.localization.Strings.ShippingPreset),
        val height: String = getString(component.localization.Strings.Height),
        val length: String = getString(component.localization.Strings.Length),
        val weight: String = getString(component.localization.Strings.Weight),
        val width: String = getString(component.localization.Strings.Width),
        val isPhysicalProduct: String = getString(component.localization.Strings.IsPhysicalProduct),
        val shipping: String = getString(component.localization.Strings.Shipping),
        val kg: String? = getString(component.localization.Strings.Kg),
        val cm: String? = getString(component.localization.Strings.Cm),
        val improveWithAi: String = getString(component.localization.Strings.ImproveWithAi),
        val deleteExplain: String = getString(component.localization.Strings.DeleteExplain),
        val status: String = getString(component.localization.Strings.Status),
        val insights: String = getString(component.localization.Strings.Insights),
        val noInsights: String = getString(component.localization.Strings.NoInsights),
        val info: String = getString(component.localization.Strings.Info),
        val createdByDesc: String = getString(component.localization.Strings.CreatedByDesc),
        val categoryOrganization: String = getString(component.localization.Strings.CategoryOrganization),
    ) {
    }

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}

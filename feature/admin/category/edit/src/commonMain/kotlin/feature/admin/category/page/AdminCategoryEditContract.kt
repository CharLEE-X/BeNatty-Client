package feature.admin.category.page

import component.localization.getString
import data.GetAllCategoriesAsMinimalQuery
import data.GetCategoryByIdQuery

object AdminCategoryEditContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val allCategories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal> = emptyList(),

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
                name = "",
            ),
            createdAt = "",
            updatedAt = "",
        ),

        val current: GetCategoryByIdQuery.GetCategoryById = original,
    )

    sealed interface Inputs {
        data class Init(val id: String) : Inputs

        data class GetCategoryById(val id: String) : Inputs
        data class GetAllCategories(val currentCategoryId: String?) : Inputs
        data object SaveEdit : Inputs
        data class ShakeErrors(
            val name: Boolean,
            val display: Boolean,
            val weight: Boolean,
            val length: Boolean,
            val width: Boolean,
            val height: Boolean
        ) : Inputs

        data object OnDeleteClick : Inputs
        data object OnSaveEditClick : Inputs
        data object OnCancelEditClick : Inputs
        data object OnGoToParentClick : Inputs
        data object OnGoToUserCreatorClick : Inputs
        data class OnParentCategoryClick(val categoryName: String) : Inputs
        data object OnGoToCreateCategoryClick : Inputs
        data object OnImproveDescriptionClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetAllCategories(val categories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal>) :
            Inputs

        data class SetOriginalCategory(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs
        data class SetCurrentCategory(val category: GetCategoryByIdQuery.GetCategoryById) : Inputs
        data class SetId(val id: String) : Inputs
        data class SetName(val fullName: String) : Inputs
        data class SetIsNameShake(val shake: Boolean) : Inputs
        data class SetDescription(val description: String) : Inputs
        data class SetParent(val parent: GetCategoryByIdQuery.Parent?) : Inputs
        data class SetDisplay(val display: Boolean) : Inputs
        data class SetIsDisplayShake(val shake: Boolean) : Inputs
        data class SetCreator(val creator: GetCategoryByIdQuery.Creator) : Inputs
        data class SetCreatedAt(val createdAt: String) : Inputs
        data class SetUpdatedAt(val updatedAt: String) : Inputs
        data class SetWeight(val weight: String) : Inputs
        data class SetIsWeightShake(val shake: Boolean) : Inputs
        data class SetLength(val length: String) : Inputs
        data class SetIsLengthShake(val shake: Boolean) : Inputs
        data class SetWidth(val width: String) : Inputs
        data class SetIsWidthShake(val shake: Boolean) : Inputs
        data class SetHeight(val height: String) : Inputs
        data class SetIsHeightShake(val shake: Boolean) : Inputs
        data class SetRequiresShipping(val requiresShipping: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToList : Events
        data class GoToUser(val id: String) : Events
        data object GoToCreateCategory : Events
        data class GoToCategory(val id: String) : Events
    }
}

val adminCategoryEditStrings: AdminCategoryPageStrings = AdminCategoryPageStrings()

data class AdminCategoryPageStrings(
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
)

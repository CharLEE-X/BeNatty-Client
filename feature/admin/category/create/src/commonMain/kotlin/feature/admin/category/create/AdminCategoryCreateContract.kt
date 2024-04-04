package feature.admin.category.create

import component.localization.getString

object AdminCategoryCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val name: String = "",
        val nameError: String? = null,
        val shakeName: Boolean = false,
    )

    sealed interface Inputs {
        data object CreateCategory : Inputs
        data class ShakeErrors(val shakeName: Boolean) : Inputs

        data object OnCreateClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetName(val name: String) : Inputs
        data class SetNameShake(val shake: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data class GoToCategory(val id: String) : Events
    }
}

val adminCategoryCreateStrings = AdminCategoryCreateStrings

object AdminCategoryCreateStrings {
    val createCategory: String = getString(component.localization.Strings.CreateCategory)
    val name: String = getString(component.localization.Strings.Name)
    val categoryNameDescription: String = getString(component.localization.Strings.CategoryNameDescription)
    val create: String = getString(component.localization.Strings.Create)
}

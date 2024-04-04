package feature.admin.tag.create

import component.localization.getString

object AdminTagCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val name: String = "",
        val nameError: String? = null,
        val shakeName: Boolean = false,
    )

    sealed interface Inputs {
        data object CreateTag : Inputs
        data class ShakeErrors(val name: Boolean) : Inputs

        data object OnCreateClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetName(val fullName: String) : Inputs
        data class SetIsNameShake(val shake: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data class GoToTag(val id: String) : Events
    }
}

val adminTagCreateStrings: AdminTagCreateStrings = AdminTagCreateStrings()

data class AdminTagCreateStrings(
    val save: String = getString(component.localization.Strings.Save),
    val edit: String = getString(component.localization.Strings.Edit),
    val discard: String = getString(component.localization.Strings.Discard),
    val delete: String = getString(component.localization.Strings.Delete),
    val createdBy: String = getString(component.localization.Strings.CreatedBy),
    val createdAt: String = getString(component.localization.Strings.CreatedAt),
    val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
    val createTag: String = getString(component.localization.Strings.CreateTag),
    val category: String = getString(component.localization.Strings.Tag),
    val details: String = getString(component.localization.Strings.Details),
    val name: String = getString(component.localization.Strings.Name),
    val create: String = getString(component.localization.Strings.Create),
    val description: String = getString(component.localization.Strings.Description),
    val none: String = getString(component.localization.Strings.None),
    val noOtherTagsToChooseFrom: String = getString(component.localization.Strings.NoOtherTagsToChooseFrom),
    val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
    val saveChanges: String = getString(component.localization.Strings.SaveChanges),
    val dismiss: String = getString(component.localization.Strings.Dismiss),
    val improveWithAi: String = getString(component.localization.Strings.ImproveWithAi),
)

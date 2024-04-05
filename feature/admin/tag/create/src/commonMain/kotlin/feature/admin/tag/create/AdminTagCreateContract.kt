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
    val createTag: String = getString(component.localization.Strings.CreateTag),
    val newTag: String = getString(component.localization.Strings.NewTag),
    val details: String = getString(component.localization.Strings.Details),
    val name: String = getString(component.localization.Strings.Name),
    val create: String = getString(component.localization.Strings.Create),
)

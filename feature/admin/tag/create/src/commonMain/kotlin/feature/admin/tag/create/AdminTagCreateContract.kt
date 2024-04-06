package feature.admin.tag.create

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

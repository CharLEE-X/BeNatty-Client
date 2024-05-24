package feature.admin.category.create

object AdminCategoryCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val name: String = "",
        val nameError: String? = null,
        val nameShake: Boolean = false,
    )

    sealed interface Inputs {
        data object CreateCategory : Inputs
        data class ShakeErrors(val name: Boolean) : Inputs

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

package feature.updatepassword

import org.koin.core.component.KoinComponent

object UpdatePasswordContract : KoinComponent {
    data class State(
        val password: String = "",
        val showPassword: Boolean = false,
        val showError: Boolean = false,
        val errorMessage: String = "",

        val screenState: ScreenState = ScreenState.Updating,
    )

    sealed interface Inputs {
        data class SetPassword(val password: String) : Inputs
        data object ToggleShowPassword : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs
        data object OnChangePasswordClick : Inputs
        data class ShowError(val message: String) : Inputs
        data object HideError : Inputs
        data object OnLoginClick : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToLogin : Events
    }

    enum class ScreenState {
        Updating,
        Success,
    }
}

package feature.updatepassword

import org.koin.core.component.KoinComponent

object UpdatePasswordContract : KoinComponent {
    data class State(
        val password: String = "",
        val showPassword: Boolean = false,
        val showError: Boolean = false,
        val errorMessage: String = "",
        val screenState: ScreenState = ScreenState.Updating,

        val strings: Strings = Strings(),
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

    data class Strings(
        val chooseNewPassword: String = "Choose a new password",
        val chooseNewPasswordDescription: String = "Please choose a new password for your account.",
        val password: String = "Password",
        val passwordHint: String = "Enter your new password",
        val changeMyPassword: String = "Change my password",
        val backTo: String = "Back to",
        val login: String = "Log in",
        val youHaveNewPassword: String = "You have a new password!",
        val youHaveNewPasswordDescription: String = "Your password has been successfully updated. You can now log in with your new password.",
        val errorPasswordLength: String = "Password must be at least 8 characters long"
    )

    enum class ScreenState {
        Updating,
        Success,
    }
}

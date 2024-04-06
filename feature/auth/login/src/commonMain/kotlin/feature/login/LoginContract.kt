package feature.login

import org.koin.core.component.KoinComponent

object LoginContract : KoinComponent {
    data class State(
        val email: String = "",
        val emailError: String? = null,

        val password: String = "",
        val passwordError: String? = null,
        val isPasswordVisible: Boolean = false,

        val isButtonDisabled: Boolean = true,

        val isLoading: Boolean = false,
    )

    sealed interface Inputs {
        data class SetEmail(val email: String) : Inputs
        data class SetPassword(val password: String) : Inputs
        data object TogglePasswordVisibility : Inputs
        data class SetIsLoading(val isLoading: Boolean) : Inputs

        data object OnGoogleClick : Inputs
        data object OnFacebookClick : Inputs
        data object OnLoginClick : Inputs
        data object OnDontHaveAccountClick : Inputs
        data object OnForgotPasswordClick : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object OnAuthenticated : Events
        data object GoToForgotPassword : Events
        data object GoToSignUp : Events
        data object GoToPrivacyPolicy : Events
        data object GoToTC : Events
    }
}

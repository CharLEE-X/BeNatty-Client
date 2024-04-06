package feature.register

import org.koin.core.component.KoinComponent

object RegisterContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,

        val name: String = "",
        val nameError: String? = null,

        val email: String = "",
        val emailError: String? = null,

        val password: String = "",
        val passwordError: String? = null,
        val isPasswordVisible: Boolean = false,

        val repeatPassword: String = "",
        val repeatPasswordError: String? = null,
        val isRepeatPasswordVisible: Boolean = false,

        val newsletterChecked: Boolean = true,

        val isButtonDisabled: Boolean = true,
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs

        data class SetName(val name: String) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetPassword(val password: String) : Inputs
        data class SetRepeatPassword(val repeatPassword: String) : Inputs
        data object TogglePasswordVisibility : Inputs
        data object ToggleRepeatPasswordVisibility : Inputs
        data object ToggleNewsletterChecked : Inputs

        data object OnGoogleClick : Inputs
        data object OnFacebookClick : Inputs
        data object OnRegisterClick : Inputs
        data object OnAlreadyHaveAccountClick : Inputs
        data object OnPrivacyPolicyClick : Inputs
        data object OnTnCClick : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object OnAuthenticated : Events
        data object GoToLogin : Events
        data object GoToPrivacyPolicy : Events
        data object GoToTnC : Events
    }
}

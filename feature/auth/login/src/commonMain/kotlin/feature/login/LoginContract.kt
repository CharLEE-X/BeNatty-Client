package feature.login

import component.localization.getString
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

        val strings: Strings = Strings()
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

    data class Strings(
        val shopName: String = getString(component.localization.Strings.ShopName),
        val appMotto: String = getString(component.localization.Strings.AppMotto),
        val logo: String = getString(component.localization.Strings.Logo),
        val email: String = getString(component.localization.Strings.Email),
        val password: String = getString(component.localization.Strings.Password),
        val forgotPassword: String = getString(component.localization.Strings.ForgotPassword),
        val dontHaveAccount: String = getString(component.localization.Strings.DontHaveAccount),
        val login: String = getString(component.localization.Strings.Login),
        val or: String = getString(component.localization.Strings.Or),
        val continueWithGoogle: String = getString(component.localization.Strings.ContinueWithGoogle),
        val continueWithFacebook: String = getString(component.localization.Strings.ContinueWithFacebook),
        val signUp: String = getString(component.localization.Strings.SignUp),
    )
}

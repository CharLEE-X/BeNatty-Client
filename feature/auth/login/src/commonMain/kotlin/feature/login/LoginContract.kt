package feature.login

import component.localization.getString
import org.koin.core.component.KoinComponent

object LoginContract : KoinComponent {
    data class State(
        val email: String = "",
        val isEmailError: Boolean = false,
        val emailError: String? = null,

        val password: String = "",
        val isPasswordError: Boolean = false,
        val passwordError: String? = null,
        val isPasswordVisible: Boolean = false,

        val isButtonDisabled: Boolean = false,

        val isLoading: Boolean = false,

        val strings: Strings = Strings(
            appName = getString(component.localization.Strings.AppName),
            appMotto = getString(component.localization.Strings.AppMotto),
            logo = getString(component.localization.Strings.Logo),
            email = getString(component.localization.Strings.Auth.Email),
            password = getString(component.localization.Strings.Auth.Password),
            forgotPassword = getString(component.localization.Strings.Auth.ForgotPassword),
            dontHaveAccount = getString(component.localization.Strings.Auth.DontHaveAccount),
            login = getString(component.localization.Strings.Auth.Login),
            or = getString(component.localization.Strings.Auth.Or),
            continueWithGoogle = getString(component.localization.Strings.Auth.ContinueWithGoogle),
            continueWithFacebook = getString(component.localization.Strings.Auth.ContinueWithFacebook),
            signUp = getString(component.localization.Strings.Auth.SignUp),
        )
    )

    sealed interface Inputs {
        data class SetEmail(val email: String) : Inputs
        data class SetPassword(val password: String) : Inputs
        data object TogglePasswordVisibility : Inputs
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data object DisableButton : Inputs

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
        val appName: String,
        val appMotto: String,
        val logo: String,
        val login: String,
        val continueWithGoogle: String,
        val continueWithFacebook: String,
        val or: String,
        val email: String,
        val password: String,
        val forgotPassword: String,
        val dontHaveAccount: String,
        val signUp: String,
    )
}

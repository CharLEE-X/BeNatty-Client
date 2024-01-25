package feature.register

import component.localization.getString
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

        val strings: Strings = Strings()
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

    data class Strings(
        val appName: String = getString(component.localization.Strings.AppName),
        val appMotto: String = getString(component.localization.Strings.AppMotto),
        val logo: String = getString(component.localization.Strings.Logo),
        val email: String = getString(component.localization.Strings.Auth.Email),
        val password: String = getString(component.localization.Strings.Auth.Password),
        val login: String = getString(component.localization.Strings.Auth.Login),
        val or: String = getString(component.localization.Strings.Auth.Or),
        val signUp: String = getString(component.localization.Strings.Auth.SignUp),
        val signUpWithGoogle: String = getString(component.localization.Strings.Auth.SignUpWithGoogle),
        val signUpWithFacebook: String = getString(component.localization.Strings.Auth.SignUpWithFacebook),
        val name: String = getString(component.localization.Strings.Auth.FullName),
        val repeatPassword: String = getString(component.localization.Strings.Auth.RepeatPassword),
        val newsletter: String = getString(component.localization.Strings.Auth.Newsletter),
        val alreadyHaveAnAccount: String = getString(component.localization.Strings.Auth.AlreadyHaveAnAccount),
        val privacyPolicy: String = getString(component.localization.Strings.Auth.PrivacyPolicy),
        val and: String = getString(component.localization.Strings.Auth.And),
        val termsOfService: String = getString(component.localization.Strings.Auth.TermsOfService),
        val bySigningUpAgree: String = getString(component.localization.Strings.Auth.BySigningUpAgree),
    )
}

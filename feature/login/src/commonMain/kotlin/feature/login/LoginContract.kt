package feature.login

import org.koin.core.component.KoinComponent

object LoginContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,

        val headerText: String = Strings.login,

        val showForgotPasswordDescription: Boolean = false,

        val showName: Boolean = false,
        val name: String = "",
        val email: String = "",

        val showPassword: Boolean = false,
        val password: String = "",
        val isPasswordVisible: Boolean = false,

        val showRepeatPassword: Boolean = false,
        val repeatPassword: String = "",
        val isRepeatPasswordVisible: Boolean = false,

        val showForgotPassword: Boolean = false,

        val showNewsletter: Boolean = false,
        val newsletterChecked: Boolean = true,

        val loginActionButtonText: String = Strings.login,

        val showAlreadyHaveAccount: Boolean = true,
        val alreadyHaveAccountText: String = Strings.alreadyHaveAnAccount,
        val alreadyHaveAccountActionText: String = Strings.login,

        val showPrivacyPolicy: Boolean = false,

        val screenState: ScreenState = ScreenState.LOGIN,
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

        data class ChangeScreenState(val screenState: ScreenState) : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs

        data object OnGoogleClick : Inputs
        data object OnFacebookClick : Inputs
        data object OnPrivacyPolicyClick : Inputs
        data object OnTnCClick : Inputs
        data object OnLoginRegisterActionButtonClick : Inputs
        data object OnAlreadyHaveAccountClick : Inputs
        data object OnForgotPasswordClick : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object OnAuthenticated : Events
        data object GotoPrivacyPolicy : Events
        data object GotoTnC : Events
    }

    enum class ScreenState {
        LOGIN,
        REGISTER,
        FORGOT_PASSWORD,
    }

    object Strings {
        val shopName: String get() = "Nat'shop"
        val shopMotto: String get() = "Here you become different from others"
        val signUp: String get() = "Sign Up"
        val login: String get() = "Log in"
        val forgotPassword: String get() = "Forgot your password?"
        val forgotPasswordDescription: String get() = "No worries we will send a recovery link to your email."
        val name: String get() = "Full name"
        val email: String get() = "Email"
        val password: String get() = "Password"
        val repeatPassword: String get() = "Repeat Password"
        val or: String get() = "OR"
        val logo: String get() = "Logo"
        val signUpWith: String get() = "Sign up with Google"
        val newsletter: String get() = "Sign up for news about sales and new arrivals"
        val bySigningUpAgree: String get() = "By signing up, you agree to our"
        val alreadyHaveAnAccount: String get() = "Already have an account?"
        val dontHaveAccount: String get() = "Don't have an account?"
        val privacyPolicy: String get() = "Privacy Policy"
        val and = "and"
        val termsOfService: String get() = "Terms of Service"
        val getResetLink: String get() = "Get a reset Link"
    }
}

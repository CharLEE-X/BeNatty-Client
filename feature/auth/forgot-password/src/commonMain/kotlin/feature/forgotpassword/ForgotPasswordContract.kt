package feature.forgotpassword

import component.localization.getString
import org.koin.core.component.KoinComponent

object ForgotPasswordContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,
        val email: String = "",
        val screenState: ScreenState = ScreenState.ForgotPassword,

        val buttonDisabled: Boolean = false,
        val showError: Boolean = false,
        val errorMessage: String? = null,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs
        data object OnGetLinkClick : Inputs
        data object OnGoToLoginClick : Inputs
        data class ShowError(val message: String) : Inputs
        data object HideError : Inputs
        data object OpenGmail : Inputs
        data object OpenOutlook : Inputs
        data object DisableButton : Inputs
    }

    sealed interface Events {
        data object GoToLogin : Events
    }

    data class Strings(
        val forgotPassword: String = getString(component.localization.Strings.ForgotPassword),
        val forgotPasswordDescription: String = getString(component.localization.Strings.ForgotPasswordDescription),
        val email: String = getString(component.localization.Strings.Email),
        val getResetLink: String = getString(component.localization.Strings.GetResetLink),
        val backTo: String = getString(component.localization.Strings.BackTo),
        val login: String = getString(component.localization.Strings.Login),
        val checkEmail: String = "Check your email",
        val checkEmailDescription: String = "We have sent a password reset link to",
        val openGmail: String = "Open Gmail",
        val openOutlook: String = "Open Outlook",
    )

    enum class ScreenState {
        ForgotPassword,
        CheckEmail,
    }
}

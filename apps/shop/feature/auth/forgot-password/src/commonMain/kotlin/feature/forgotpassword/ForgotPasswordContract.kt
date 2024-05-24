package feature.forgotpassword

import org.koin.core.component.KoinComponent

object ForgotPasswordContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,
        val email: String = "",
        val screenState: ScreenState = ScreenState.ForgotPassword,

        val buttonDisabled: Boolean = false,
        val showError: Boolean = false,
        val errorMessage: String? = null,
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

    enum class ScreenState {
        ForgotPassword,
        CheckEmail,
    }
}

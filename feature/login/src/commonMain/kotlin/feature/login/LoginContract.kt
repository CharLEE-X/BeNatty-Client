package feature.login

import org.koin.core.component.KoinComponent

object LoginContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,
        val login: String = "",
        val password: String = "",
        val repeatPassword: String = "",
        val isPasswordVisible: Boolean = false,
        val isRepeatPasswordVisible: Boolean = false,
        val screenState: ScreenState = ScreenState.LOGIN,
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetLogin(val login: String) : Inputs
        data class SetPassword(val password: String) : Inputs
        data class SetRepeatPassword(val repeatPassword: String) : Inputs
        data object TogglePasswordVisibility : Inputs
        data object ToggleRepeatPasswordVisibility : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs
        data object Login : Inputs
        data object Register : Inputs
        data object ForgotPassword : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object OnAuthenticated : Events
    }

    enum class ScreenState {
        LOGIN,
        REGISTER,
        FORGOT_PASSWORD,
    }
}

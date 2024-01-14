package component.localization

sealed interface Strings {
    data object AppName : Strings
    data object CompanyName : Strings
    data object AppVersion : Strings
    data object AppCopyright : Strings

    data object NoImage : Strings
    data object CloseButton : Strings
    data object Apply : Strings
    data object ShowMore : Strings
    data object RemoveFilter : Strings
    data object AddFilter : Strings

    sealed interface Login : Strings {
        data object WelcomeMessage : Login
        data object LoginButton : Login
        data object LogoutButton : Login
        data object RegisterButton : Login
        data object EmailLabel : Login
        data object PasswordLabel : Login
        data object ConfirmPasswordLabel : Login
        data object ForgotPasswordLabel : Login
        data object SendButton : Login
        data object GoBackToLoginButton : Login
        data object DontHaveAnAccount : Login
    }

    data object Unknown : Strings
    data object Save : Strings
    data object Add : Strings
}

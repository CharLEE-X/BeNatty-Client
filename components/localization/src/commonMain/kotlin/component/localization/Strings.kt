package component.localization

sealed interface Strings {
    data object AppName : Strings
    data object AppMotto : Strings
    data object CompanyName : Strings
    data object AppVersion : Strings
    data object AppCopyright : Strings

    data object NoImage : Strings
    data object CloseButton : Strings
    data object Apply : Strings
    data object ShowMore : Strings
    data object RemoveFilter : Strings
    data object AddFilter : Strings
    data object Logo : Strings

    sealed interface Auth : Strings {
        data object Login : Auth
        data object SignUp : Auth
        data object ContinueWithGoogle : Auth
        data object ContinueWithFacebook : Auth
        data object Or : Auth
        data object Email : Auth
        data object Password : Auth
        data object ForgotPassword : Auth
        data object DontHaveAccount : Auth

        data object SignUpWithGoogle: Auth
        data object SignUpWithFacebook: Auth
        data object FullName: Auth
        data object RepeatPassword: Auth
        data object Newsletter: Auth
        data object AlreadyHaveAnAccount: Auth
        data object PrivacyPolicy: Auth
        data object And: Auth
        data object TermsOfService: Auth
        data object BySigningUpAgree: Auth

        data object ForgotPasswordDescription : Auth
        data object GetResetLink : Auth

        data object CheckEmail : Auth
        data object CheckEmailDescription : Auth
        data object OpenGmail : Auth
        data object OpenOutlook : Auth
        data object BackTo : Auth
    }

    data object Unknown : Strings
    data object Save : Strings
    data object Add : Strings
}

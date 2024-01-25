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

    data object Unknown : Strings
    data object Save : Strings
    data object Add : Strings

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

        data object SignUpWithGoogle : Auth
        data object SignUpWithFacebook : Auth
        data object FullName : Auth
        data object RepeatPassword : Auth
        data object Newsletter : Auth
        data object AlreadyHaveAnAccount : Auth
        data object PrivacyPolicy : Auth
        data object And : Auth
        data object TermsOfService : Auth
        data object BySigningUpAgree : Auth

        data object ForgotPasswordDescription : Auth
        data object GetResetLink : Auth

        data object CheckEmail : Auth
        data object CheckEmailDescription : Auth
        data object OpenGmail : Auth
        data object OpenOutlook : Auth
        data object BackTo : Auth
    }

    sealed interface Account : Strings {
        data object Phone : Account
        data object PersonalDetails : Account
        data object Profile : Account
        data object OldPassword : Account
        data object NewPassword : Account
        data object Address : Account
        data object AdditionalInformation : Account
        data object PostalCode : Account
        data object City : Account
        data object State : Account
        data object Country : Account
    }

    sealed interface Navigation: Strings {
        data object PromoText : Navigation
        data object HelpAndFaq : Navigation
        data object CurrencyEnUs : Navigation
        data object Search : Navigation
        data object Login : Navigation
        data object Orders : Navigation
        data object Returns : Navigation
        data object Wishlist : Navigation
        data object Profile : Navigation
        data object Logout : Navigation
    }
}

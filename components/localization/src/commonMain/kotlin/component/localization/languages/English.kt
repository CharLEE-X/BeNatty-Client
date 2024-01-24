package component.localization.languages

import component.localization.Strings
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import localization.BuildKonfig


internal fun englishLanguage(
    key: Strings,
    appName: String = BuildKonfig.appName,
    companyName: String = BuildKonfig.orgName,
    currentYear: Int = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
): String = when (key) {
    Strings.AppName -> appName
    Strings.AppMotto -> "Here you become different from others"
    Strings.CompanyName -> companyName
    Strings.AppCopyright -> "© $currentYear $companyName"
    Strings.AppVersion -> "0.1.0"

    Strings.Unknown -> "Unknown"
    Strings.Save -> "Save"
    Strings.Add -> "Add"

    Strings.NoImage -> "No image"
    Strings.CloseButton -> "Close"
    Strings.Apply -> "Apply"
    Strings.ShowMore -> "Show more"
    Strings.RemoveFilter -> "Remove filter"
    Strings.AddFilter -> "Add filter"
    Strings.Logo -> "Logo"

    Strings.Auth.Login -> "Log in"
    Strings.Auth.ContinueWithGoogle -> "Continue with Google"
    Strings.Auth.ContinueWithFacebook -> "Continue with Facebook"
    Strings.Auth.Or -> "OR"
    Strings.Auth.Email -> "Email"
    Strings.Auth.Password -> "Password"
    Strings.Auth.ForgotPassword -> "Forgot your password?"
    Strings.Auth.DontHaveAccount -> "Don't have an account?"
    Strings.Auth.SignUp -> "Sign Up"

    Strings.Auth.SignUpWithGoogle -> "Sign up with Google"
    Strings.Auth.SignUpWithFacebook -> "Sign up with Facebook"
    Strings.Auth.FullName -> "Full name"
    Strings.Auth.RepeatPassword -> "Repeat Password"
    Strings.Auth.Newsletter -> "Sign up for news about sales and new arrivals"
    Strings.Auth.AlreadyHaveAnAccount -> "Already have an account?"
    Strings.Auth.PrivacyPolicy -> "Privacy Policy"
    Strings.Auth.And -> "and"
    Strings.Auth.TermsOfService -> "Terms of Service"
    Strings.Auth.BySigningUpAgree -> "By signing up, you agree to our"

    Strings.Auth.ForgotPasswordDescription -> "Wo worries, we'll send recovery link to your email."
    Strings.Auth.GetResetLink -> "Get a reset link"

    Strings.Auth.CheckEmail -> "Check your email"
    Strings.Auth.CheckEmailDescription -> "We have sent a password reset link to"
    Strings.Auth.OpenGmail -> "Open Gmail"
    Strings.Auth.OpenOutlook -> "Open Outlook"
    Strings.Auth.BackTo -> "Back to"
}

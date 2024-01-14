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

    Strings.Login.WelcomeMessage -> "Welcome to"
    Strings.Login.LoginButton -> "Login"
    Strings.Login.LogoutButton -> "Logout"
    Strings.Login.RegisterButton -> "Register"
    Strings.Login.EmailLabel -> "Email"
    Strings.Login.PasswordLabel -> "Password"
    Strings.Login.ConfirmPasswordLabel -> "Confirm Password"
    Strings.Login.ForgotPasswordLabel -> "Forgot Password?"
    Strings.Login.SendButton -> "Send"
    Strings.Login.GoBackToLoginButton -> "Go back to Login"
    Strings.Login.DontHaveAnAccount -> "Don't have an account?"
}

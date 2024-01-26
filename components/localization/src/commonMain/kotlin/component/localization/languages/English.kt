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
    Strings.Login -> "Log in"
    Strings.ContinueWithGoogle -> "Continue with Google"
    Strings.ContinueWithFacebook -> "Continue with Facebook"
    Strings.Or -> "OR"
    Strings.Email -> "Email"
    Strings.Password -> "Password"
    Strings.ForgotPassword -> "Forgot your password?"
    Strings.DontHaveAccount -> "Don't have an account?"
    Strings.SignUp -> "Sign Up"
    Strings.SignUpWithGoogle -> "Sign up with Google"
    Strings.SignUpWithFacebook -> "Sign up with Facebook"
    Strings.FullName -> "Full name"
    Strings.RepeatPassword -> "Repeat Password"
    Strings.Newsletter -> "Sign up for news about sales and new arrivals"
    Strings.AlreadyHaveAnAccount -> "Already have an account?"
    Strings.PrivacyPolicy -> "Privacy Policy"
    Strings.And -> "and"
    Strings.TermsOfService -> "Terms of Service"
    Strings.BySigningUpAgree -> "By signing up, you agree to our"
    Strings.ForgotPasswordDescription -> "Wo worries, we'll send recovery link to your email."
    Strings.GetResetLink -> "Get a reset link"
    Strings.CheckEmail -> "Check your email"
    Strings.CheckEmailDescription -> "We have sent a password reset link to"
    Strings.OpenGmail -> "Open Gmail"
    Strings.OpenOutlook -> "Open Outlook"
    Strings.BackTo -> "Back to"
    Strings.Phone -> "Phone number"
    Strings.PersonalDetails -> "Personal details"
    Strings.Profile -> "Profile"
    Strings.OldPassword -> "Old password"
    Strings.NewPassword -> "New password"
    Strings.Address -> "Address"
    Strings.AdditionalInformation -> "Additional information (e.g.Company)"
    Strings.PostalCode -> "Postal code"
    Strings.City -> "City"
    Strings.State -> "State"
    Strings.Country -> "Country"
    Strings.PromoText -> "Free shipping on orders over £50"
    Strings.HelpAndFaq -> "Help & FAQ"
    Strings.CurrencyEnUs -> "EN, $"
    Strings.Search -> "Search"
    Strings.Login -> "Login"
    Strings.Orders -> "Orders"
    Strings.Returns -> "Returns"
    Strings.Wishlist -> "Wishlist"
    Strings.Profile -> "Profile"
    Strings.Logout -> "Log out"
    Strings.Subscribe -> "Subscribe to be first to know about our special offers"
    Strings.Accessibility -> "Accessibility"
    Strings.AboutUsSmall -> "About us"
    Strings.AboutUs -> "About Us"
    Strings.Career -> "Career"
    Strings.ContactUs -> "Contact Us"
    Strings.CyberSecurity -> "Cyber Security"
    Strings.DeliverTo -> "Deliver to"
    Strings.English -> "English"
    Strings.FAQs -> "FAQs"
    Strings.Language -> "Language"
    Strings.Press -> "Press"
    Strings.Shipping -> "Shipping"
    Strings.TrackOrder -> "Track Order"
    Strings.Help -> "Help"
}

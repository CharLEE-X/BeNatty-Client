package web.components.sections.footer

import component.localization.getString
import core.util.currentYear
import org.koin.core.component.KoinComponent

object FooterContract : KoinComponent {
    data class State(
        val isLoading: Boolean = false,
        val email: String = "",
        val emailError: String? = null,

        val languageImageUrl: String = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",

        val year: Int = currentYear(),
        val strings: Strings = Strings(),
    )

    sealed interface Inputs {
        data class SetEmail(val email: String) : Inputs
        data object OnEmailSend : Inputs
        data object OnPrivacyPolicyClicked : Inputs
        data object OnTermsOfServiceClicked : Inputs
        data object OnAccessibilityClicked : Inputs
        data object OnTrackOrderClick : Inputs
        data object OnShippingClick : Inputs
        data object OnReturnsClick : Inputs
        data object OnFAQsClick : Inputs
        data object OnContactUsClick : Inputs
        data object OnAboutUsClick : Inputs
        data object OnCareerClick : Inputs
        data object OnCyberSecurityClick : Inputs
        data object OnPressClick : Inputs
        data object OnCurrencyClick : Inputs
        data object OnLanguageClick : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToPrivacyPolicy : Events
        data object GoToTermsOfService : Events
        data object GoToAccessibility : Events
        data object GoToTrackOrder : Events
        data object GoToShipping : Events
        data object GoToReturns : Events
        data object GoToFAQs : Events
        data object GoToContactUs : Events
        data object GoToAboutUs : Events
        data object GoToCareer : Events
        data object GoToCyberSecurity : Events
        data object GoToPress : Events
    }

    data class Strings(
        val subscribe: String = getString(component.localization.Strings.Subscribe),
        val email: String = getString(component.localization.Strings.Email),
        val companyName: String = getString(component.localization.Strings.CompanyName),
        val privacyPolicy: String = getString(component.localization.Strings.PrivacyPolicy),
        val termsOfService: String = getString(component.localization.Strings.TermsOfService),
        val accessibility: String = getString(component.localization.Strings.Accessibility),
        val trackOrder: String = getString(component.localization.Strings.TrackOrder),
        val shipping: String = getString(component.localization.Strings.Shipping),
        val returns: String = getString(component.localization.Strings.Returns),
        val faQs: String = getString(component.localization.Strings.FAQs),
        val contactUs: String = getString(component.localization.Strings.ContactUs),
        val aboutUsSmall: String = getString(component.localization.Strings.AboutUs),
        val aboutUs: String = getString(component.localization.Strings.AboutUs),
        val career: String = getString(component.localization.Strings.Career),
        val cyberSecurity: String = getString(component.localization.Strings.CyberSecurity),
        val press: String = getString(component.localization.Strings.Press),
        val deliverTo: String = getString(component.localization.Strings.DeliverTo),
        val language: String = getString(component.localization.Strings.Language),
        val english: String = getString(component.localization.Strings.English),
        val help: String = getString(component.localization.Strings.Help),
    )
}

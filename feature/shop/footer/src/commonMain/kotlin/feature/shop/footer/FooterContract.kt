package feature.shop.footer

import component.localization.getString
import core.util.currentYear
import feature.shop.footer.model.ContactInfo
import feature.shop.footer.model.OpeningTimes
import feature.shop.footer.model.PaymentMethod
import feature.shop.footer.model.dummyPaymentMethods
import org.koin.core.component.KoinComponent

object FooterContract : KoinComponent {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
        val isAdmin: Boolean = false,
        val showCareer: Boolean = false,
        val showCyberSecurity: Boolean = false,
        val showPress: Boolean = false,
        val year: Int = currentYear(),

        val currentCountryText: String = strings.unitedKingdom,
        val currentLanguageText: String = strings.english,
        val countryImageUrl: String = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",
        val languageImageUrl: String = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",

        val paymentMethods: List<PaymentMethod> = dummyPaymentMethods,
        val openingTimes: OpeningTimes = OpeningTimes(
            dayFrom = "Monday",
            dayTo = "Friday",
            open = "9:00",
            close = "18:00",
        ),
        val contactInfo: ContactInfo = ContactInfo(
            companyWebsite = "https://charleex.com",
            email = "contact@${strings.companyName}.com",
            phone = "+44 123 456 7890",
        ),
    )

    sealed interface Inputs {
        data object Init : Inputs

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
        data object OnGoToAdminHome : Inputs
        data object OnCompanyNameClick : Inputs
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
        data object GoToAdminHome : Events
        data object GoToCompanyWebsite : Events
    }

    data class Strings(
        val companyName: String = getString(component.localization.Strings.CompanyName),
        val company: String = getString(component.localization.Strings.Company),
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
        val currencyEnUs: String = getString(component.localization.Strings.CurrencyEnUs),
        val unitedKingdom: String = getString(component.localization.Strings.UnitedKingdom),
        val admin: String = getString(component.localization.Strings.Admin),
        val followUs: String = getString(component.localization.Strings.FollowUs),
        val canWeHelpYou: String = getString(component.localization.Strings.CanWeHelpYou),
        val startChat: String = getString(component.localization.Strings.StartChat),
        val from: String = getString(component.localization.Strings.From),
        val to: String = getString(component.localization.Strings.To),
        val tel: String = getString(component.localization.Strings.Tel),
        val sendEmail: String = getString(component.localization.Strings.SendEmail),
        val weWillReply: String = getString(component.localization.Strings.WeWillReply),
    ) {
    }
}

data class FooterRoutes(
    val goToAboutUs: () -> Unit,
    val goToAccessibility: () -> Unit,
    val goToCareer: () -> Unit,
    val goToContactUs: () -> Unit,
    val goToCyberSecurity: () -> Unit,
    val goToFAQs: () -> Unit,
    val goToPress: () -> Unit,
    val goToPrivacyPolicy: () -> Unit,
    val goToReturns: () -> Unit,
    val goToShipping: () -> Unit,
    val goToTermsOfService: () -> Unit,
    val goToTrackOrder: () -> Unit,
    val goToAdminHome: () -> Unit,
)

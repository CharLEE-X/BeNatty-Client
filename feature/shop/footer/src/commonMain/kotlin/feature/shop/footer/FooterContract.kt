package feature.shop.footer

import component.localization.Strings
import component.localization.getString
import core.util.currentYear
import data.GetConfigQuery
import feature.shop.footer.model.PaymentMethod
import feature.shop.footer.model.dummyPaymentMethods
import org.koin.core.component.KoinComponent

object FooterContract : KoinComponent {
    data class State(
        val isLoading: Boolean = true,
        val isAdmin: Boolean = false,
        val year: Int = currentYear(),

        val currentCountryText: String = getString(Strings.UnitedKingdom),
        val currentLanguageText: String = getString(Strings.English),
        val countryImageUrl: String = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",
        val languageImageUrl: String = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",

        val paymentMethods: List<PaymentMethod> = dummyPaymentMethods,

        val companyInfo: GetConfigQuery.CompanyInfo? = null,
        val footerConfig: GetConfigQuery.FooterConfig? = null,

        val connectEmail: String = "",
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object CheckUserRole : Inputs
        data object GetConfig : Inputs

        data object OnConnectEmailSend : Inputs
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
        data object OnTickerClick : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetCompanyInfo(val companyInfo: GetConfigQuery.CompanyInfo) : Inputs
        data class SetFooterConfig(val footerConfig: GetConfigQuery.FooterConfig) : Inputs
        data class SetConnectEmail(val email: String) : Inputs
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
        data class GoToCompanyWebsite(val url: String) : Events
        data object GoToCatalogue : Events
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
    val goToCatalogue: () -> Unit,
)

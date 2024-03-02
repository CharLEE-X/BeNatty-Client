package feature.shop.home

import component.localization.getString
import data.GetLandingConfigQuery

object HomeContract {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,

        val landingConfig: GetLandingConfigQuery.GetLandingConfig? = null,
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchLandingConfig : Inputs
        data object FetchProducts : Inputs

        data class OnCollageItemClick(val item: GetLandingConfigQuery.CollageItem) : Inputs
        data object OnPrivacyPolicyClick : Inputs
        data object OnTermsOfServiceClick : Inputs
        data object OnBannerLeftClick : Inputs
        data object OnBannerRightClick : Inputs

        data class OnEmailChange(val email: String) : Inputs
        data object OnEmailSend : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetLandingConfig(val landingConfig: GetLandingConfigQuery.GetLandingConfig) : Inputs
        data class SetProducts(val products: List<String>) : Inputs
        data class SetEmail(val email: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToPrivacyPolicy : Events
        data object GoToTermsOfService : Events
        data object GoToCatalogue : Events
    }

    data class Strings(
        val home: String = getString(component.localization.Strings.Home),
        val subscribeToOurNewsletter: String = getString(component.localization.Strings.SubscribeToOurNewsletter),
        val email: String = getString(component.localization.Strings.Email),
        val shopNow: String = getString(component.localization.Strings.ShopNow),
        val beFirstToGetLatestOffers: String = getString(component.localization.Strings.BeFirstToGetLatestOffers),
        val byAgreeing: String = getString(component.localization.Strings.ByAgreeing),
        val privacyPolicy: String = getString(component.localization.Strings.PrivacyPolicy),
        val and: String = getString(component.localization.Strings.And),
        val termsOfService: String = getString(component.localization.Strings.TermsOfService),
        val trendingNow: String = getString(component.localization.Strings.TrendingNow),
        val exploreLatestFashionTrendsHere: String = getString(component.localization.Strings.ExploreLatestFashionTrendsHere),
        val ecoFriendlyClothing: String = getString(component.localization.Strings.EcoFriendlyClothing),
        val cottonNoArtificialIngredients: String = getString(component.localization.Strings.CottonNoArtificialIngredients),
    ) {
    }
}

data class HomeRoutes(
    val home: () -> Unit,
    val privacyPolicy: () -> Unit,
    val termsOfService: () -> Unit,
    val catalogue: () -> Unit,
)

package feature.shop.home

import data.GetLandingConfigQuery

object HomeContract {
    data class State(
        val isLoading: Boolean = true,

        val landingConfig: GetLandingConfigQuery.GetLandingConfig? = null,
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,
        val currentMediaIndex: Int? = null,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchLandingConfig : Inputs
        data object FetchProducts : Inputs

        data class OnCollageItemClick(val item: GetLandingConfigQuery.SlideshowItem) : Inputs
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
}

data class HomeRoutes(
    val home: () -> Unit,
    val privacyPolicy: () -> Unit,
    val termsOfService: () -> Unit,
    val catalogue: () -> Unit,
)

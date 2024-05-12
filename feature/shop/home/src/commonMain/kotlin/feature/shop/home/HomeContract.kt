package feature.shop.home

import component.localization.Strings
import component.localization.getString
import data.GetLandingConfigQuery

object HomeContract {
    data class State(
        val isLoading: Boolean = true,

        val landingConfig: GetLandingConfigQuery.GetLandingConfig = GetLandingConfigQuery.GetLandingConfig(
            slideshowItems = listOf(), topCategoriesSection = GetLandingConfigQuery.TopCategoriesSection(
                left = GetLandingConfigQuery.Left(media = null, title = null),
                middle = GetLandingConfigQuery.Middle(media = null, title = null),
                right = GetLandingConfigQuery.Right(media = null, title = null)
            )

        ),
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,

        val categorySection: List<CategoryItem> = listOf(
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks1.jpg?v=1614301039&width=600",
                title = getString(Strings.GetTops)
            ),
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks2.jpg?v=1614301039&width=600",
                title = getString(Strings.GetBottoms)
            ),
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks3.jpg?v=1614301039&width=600",
                title = getString(Strings.GetDresses)
            )
        ),
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

    data class CategoryItem(val title: String, val url: String)
}

data class HomeRoutes(
    val home: () -> Unit,
    val privacyPolicy: () -> Unit,
    val termsOfService: () -> Unit,
    val catalogue: () -> Unit,
)

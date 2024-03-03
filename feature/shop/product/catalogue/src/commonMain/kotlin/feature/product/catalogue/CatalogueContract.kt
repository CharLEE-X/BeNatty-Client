package feature.product.catalogue

import component.localization.getString
import data.GetCatalogueConfigQuery
import data.GetCataloguePageQuery
import kotlinx.serialization.Serializable

private val defaultPageInfo = GetCataloguePageQuery.Info(
    count = 0,
    pages = 0,
    prev = null,
    next = null,
)

object CatalogueContract {
    data class State(
        internal val variant: Variant = Variant.Catalogue,

        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
        val pageSize: Int = 10,
        val showBanner: Boolean = variant !is Variant.Search,
        val showSearch: Boolean = variant is Variant.Search,
        val bannerTitle: String? = null,
        val bannerImageUrl: String? = null,

        val catalogueConfig: GetCatalogueConfigQuery.GetCatalogueConfig? = null,
        val products: List<GetCataloguePageQuery.Product> = emptyList(),
        val pageInfo: GetCataloguePageQuery.Info = defaultPageInfo,
    )

    sealed interface Inputs {
        data class Init(val variant: Variant) : Inputs
        data object FetchCatalogueConfig : Inputs
        data class FetchProducts(val page: Int) : Inputs

        data class OnGoToProductClicked(val productId: String) : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetProducts(val products: List<GetCataloguePageQuery.Product>) : Inputs
        data class SetPageInfo(val pageInfo: GetCataloguePageQuery.Info) : Inputs
        data class SetCatalogueConfig(val catalogueConfig: GetCatalogueConfigQuery.GetCatalogueConfig) : Inputs
        data class SetVariant(val variant: Variant) : Inputs
        data class SetShowBanner(val showBanner: Boolean) : Inputs
        data class SetShowSearch(val showSearch: Boolean) : Inputs
        data class SetBanner(val bannerTitle: String?, val bannerImageUrl: String?) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data class GoToProduct(val productId: String) : Events
    }

    data class Strings(
        val productPage: String = getString(component.localization.Strings.ProductPage),
        val catalogue: String = getString(component.localization.Strings.Catalogue),
        val popularRightNow: String = getString(component.localization.Strings.PopularRightNow),
        val sales: String = getString(component.localization.Strings.Sales),
        val kids: String = getString(component.localization.Strings.Kids),
        val men: String = getString(component.localization.Strings.Men),
        val searchResults: String = getString(component.localization.Strings.SearchResults),
        val women: String = getString(component.localization.Strings.Women),
    )

}

@Serializable
sealed class Variant {
    @Serializable
    data object Catalogue : Variant()

    @Serializable
    data object Popular : Variant()

    @Serializable
    data object Sales : Variant()

    @Serializable
    data object Men : Variant()

    @Serializable
    data object Women : Variant()

    @Serializable
    data object Kids : Variant()

    @Serializable
    data class Search(val query: String) : Variant()
}

data class CatalogueRoutes(
    val onError: suspend (String) -> Unit,
    val goToProduct: suspend (String) -> Unit,
)

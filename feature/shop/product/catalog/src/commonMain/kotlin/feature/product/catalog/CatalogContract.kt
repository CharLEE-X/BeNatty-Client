package feature.product.catalog

import component.localization.getString
import data.GetAllCategoriesAsMinimalQuery
import data.GetCatalogConfigQuery
import data.GetCatalogPageQuery
import kotlinx.serialization.Serializable

private val defaultPageInfo = GetCatalogPageQuery.Info(
    count = 0,
    pages = 0,
    prev = null,
    next = null,
)

object CatalogContract {
    data class State(
        internal val variant: Variant = Variant.Catalog,

        val strings: Strings = Strings(),
        val isLoading: Boolean = true,
        val pageSize: Int = 10,
        val showBanner: Boolean = variant !is Variant.Search,
        val showSearch: Boolean = variant is Variant.Search,
        val bannerTitle: String? = null,
        val bannerImageUrl: String? = null,

        val selectedCategories: List<String> = emptyList(),
        val selectedColors: List<String> = emptyList(),
        val selectedSizes: List<String> = emptyList(),
        val selectedPriceFrom: String? = null,
        val selectedPriceTo: String? = null,

        val catalogConfig: GetCatalogConfigQuery.GetCatalogConfig? = null,
        val categories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal> = emptyList(),
        val products: List<GetCatalogPageQuery.Product> = emptyList(),
        val pageInfo: GetCatalogPageQuery.Info = defaultPageInfo,
    )

    sealed interface Inputs {
        data class Init(val variant: Variant) : Inputs
        data object FetchCatalogueConfig : Inputs
        data object FetchCategories : Inputs
        data class FetchProducts(val page: Int) : Inputs

        data class OnGoToProductClicked(val productId: String) : Inputs
        data class OnCategoryClicked(val categoryId: String) : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetCategories(
            val categories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal>
        ) : Inputs

        data class SetProducts(val products: List<GetCatalogPageQuery.Product>) : Inputs
        data class SetPageInfo(val pageInfo: GetCatalogPageQuery.Info) : Inputs
        data class SetCatalogueConfig(val catalogueConfig: GetCatalogConfigQuery.GetCatalogConfig) : Inputs
        data class SetVariant(val variant: Variant) : Inputs
        data class SetShowBanner(val showBanner: Boolean) : Inputs
        data class SetShowSearch(val showSearch: Boolean) : Inputs
        data class SetBanner(val bannerTitle: String?, val bannerImageUrl: String?) : Inputs
        data class SetSelectedCategories(val categories: List<String>) : Inputs
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
        val matchAll: String = getString(component.localization.Strings.MatchAll),
    )

}

@Serializable
sealed class Variant {
    @Serializable
    data object Catalog : Variant()

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

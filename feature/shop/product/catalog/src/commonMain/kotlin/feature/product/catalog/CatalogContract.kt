package feature.product.catalog

import core.models.Currency
import data.GetAllCatalogFilterOptionsQuery
import data.GetCatalogConfigQuery
import data.GetCatalogPageQuery
import data.GetCurrentCatalogFilterOptionsQuery
import data.GetTrendingNowProductsQuery
import data.type.Color
import data.type.ProductsSort
import data.type.Size
import data.type.Trait
import kotlinx.serialization.Serializable

object CatalogContract {
    data class State(
        internal val catalogVariant: CatalogVariant = CatalogVariant.Catalog,

        val isCatalogConfigLoading: Boolean = true,

        val pageSize: Int = 10,
        val showSearch: Boolean = catalogVariant is CatalogVariant.Search,
        val showBanner: Boolean = catalogVariant !is CatalogVariant.Search,
        val bannerTitle: String? = null,
        val bannerImageUrl: String? = null,

        val catalogConfig: GetCatalogConfigQuery.GetCatalogConfig? = null,
        val products: List<GetCatalogPageQuery.Product> = emptyList(),
        val pageInfo: GetCatalogPageQuery.Info = GetCatalogPageQuery.Info(
            count = 0, pages = 0, prev = null, next = null
        ),
        val trendingNowProducts: List<GetTrendingNowProductsQuery.Product> = emptyList(),

        val sortBy: ProductsSort = ProductsSort.Featured,

        // Filters

        val allCatalogFilterOptions: GetAllCatalogFilterOptionsQuery.GetAllCatalogFilterOptions =
            GetAllCatalogFilterOptionsQuery.GetAllCatalogFilterOptions(
                categories = emptyList(),
                colors = emptyList(),
                sizes = emptyList(),
            ),

        val currentVariantOptions: GetCurrentCatalogFilterOptionsQuery.GetCurrentCatalogFilterOptions =
            GetCurrentCatalogFilterOptionsQuery.GetCurrentCatalogFilterOptions(
                total = 0,
                categories = emptyList(),
                colors = emptyList(),
                sizes = emptyList(),
                highestPrice = null,
            ),

        val query: String = "",

        val selectedCategoryIds: List<String> = listOf(),
        val showCategoryReset: Boolean = false,

        val selectedColors: List<Color> = listOf(),
        val showColorReset: Boolean = false,

        val selectedSizes: List<Size> = listOf(),
        val showSizeReset: Boolean = false,

        val priceFrom: String? = null,
        val priceTo: String? = null,
        val showPriceReset: Boolean = false,

        val selectedTraits: List<Trait> = emptyList(),
        val showTraitsReset: Boolean = false,

        val currency: Currency = Currency("£", "GBP"),
    )

    sealed interface Inputs {
        data class Init(val catalogVariant: CatalogVariant) : Inputs
        data object FetchCatalogConfig : Inputs
        data object FetchAllCatalogFilterOptions : Inputs
        data object FetchTrendingProductsNow : Inputs
        data class FetchCurrentCatalogFilterOptions(
            val categories: List<String>,
            val colors: List<Color>,
            val sizes: List<Size>,
            val priceFrom: String?,
            val priceTo: String?,
            val traits: List<Trait>,
        ) : Inputs
        data class FetchProducts(
            val page: Int,
            val query: String,
            val categoryFilters: List<String>,
            val colorFilters: List<Color>,
            val sizeFilters: List<Size>,
            val priceFrom: String?,
            val priceTo: String?,
            val traits: List<Trait>,
        ) : Inputs

        data object LoadMoreProducts : Inputs

        data class OnGoToProductClicked(val productId: String) : Inputs
        data class OnQueryChanged(val query: String) : Inputs
        data class OnCategoryClicked(val categoryId: String) : Inputs
        data object OnCategoryResetClicked : Inputs
        data class OnColorClicked(val color: Color) : Inputs
        data object OnColorResetClicked : Inputs
        data class OnSizeClicked(val size: Size) : Inputs
        data object OnSizeResetClicked : Inputs
        data object OnPriceResetClicked : Inputs
        data class OnSortBySelected(val sortBy: String) : Inputs
        data class OnPriceFromChanged(val priceFrom: String) : Inputs
        data class OnPriceToChanged(val priceTo: String) : Inputs
        data class OnTraitClicked(val trait: Trait) : Inputs

        data class SetIsCatalogConfigLoading(val loading: Boolean) : Inputs
        data class SetProducts(val products: List<GetCatalogPageQuery.Product>) : Inputs
        data class SetTrendingProducts(val products: List<GetTrendingNowProductsQuery.Product>) : Inputs
        data class SetPageInfo(val pageInfo: GetCatalogPageQuery.Info) : Inputs
        data class SetCatalogueConfig(val catalogueConfig: GetCatalogConfigQuery.GetCatalogConfig) : Inputs
        data class SetAllCatalogFilterOptions(val options: GetAllCatalogFilterOptionsQuery.GetAllCatalogFilterOptions) :
            Inputs

        data class SetVariant(val catalogVariant: CatalogVariant) : Inputs
        data class SetShowBanner(val showBanner: Boolean) : Inputs
        data class SetShowSearch(val showSearch: Boolean) : Inputs
        data class SetBanner(val bannerTitle: String?, val bannerImageUrl: String?) : Inputs
        data class SetCurrentVariantOptions(
            val variantOptions: GetCurrentCatalogFilterOptionsQuery.GetCurrentCatalogFilterOptions
        ) : Inputs

        data class SetQuery(val query: String) : Inputs
        data class SetSelectedCategories(val categories: List<String>) : Inputs
        data class SetSelectedColors(val colors: List<Color>) : Inputs
        data class SetSelectedSizes(val sizes: List<Size>) : Inputs
        data class SetPriceFrom(val priceFrom: String?) : Inputs
        data class SetPriceTo(val priceTo: String?) : Inputs
        data class SetSortBy(val sortBy: ProductsSort) : Inputs
        data class SetTraits(val traits: List<Trait>) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data class GoToProduct(val productId: String) : Events
    }
}

@Serializable
sealed class CatalogVariant {
    @Serializable
    data object Catalog : CatalogVariant()

    @Serializable
    data object Popular : CatalogVariant()

    @Serializable
    data object Sales : CatalogVariant()

    @Serializable
    data object Men : CatalogVariant()

    @Serializable
    data object Women : CatalogVariant()

    @Serializable
    data object Kids : CatalogVariant()

    @Serializable
    data class Search(val query: String) : CatalogVariant()
}

data class CatalogueRoutes(
    val onError: suspend (String) -> Unit,
    val goToProduct: suspend (String) -> Unit,
)

package feature.product.catalog

import data.GetCatalogConfigQuery
import data.GetCatalogPageQuery
import data.GetProductVariantOptionsQuery.GetProductVariantOptions
import data.type.Color
import data.type.ProductsSort
import data.type.Size
import kotlinx.serialization.Serializable

private val defaultPageInfo = GetCatalogPageQuery.Info(
    count = 0,
    pages = 0,
    prev = null,
    next = null,
)

object CatalogContract {
    data class State(
        internal val catalogVariant: CatalogVariant = CatalogVariant.Catalog,

        val isLoading: Boolean = true,
        val pageSize: Int = 10,
        val showBanner: Boolean = catalogVariant !is CatalogVariant.Search,
        val showSearch: Boolean = catalogVariant is CatalogVariant.Search,
        val bannerTitle: String? = null,
        val bannerImageUrl: String? = null,

        val catalogConfig: GetCatalogConfigQuery.GetCatalogConfig? = null,

        val products: List<GetCatalogPageQuery.Product> = emptyList(),
        val pageInfo: GetCatalogPageQuery.Info = defaultPageInfo,

        val variantOptions: GetProductVariantOptions = GetProductVariantOptions(
            categories = emptyList(),
            colors = emptyList(),
            sizes = emptyList(),
            highestPrice = null,
        ),

        val sortBy: ProductsSort = ProductsSort.Featured,

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
    )

    sealed interface Inputs {
        data class Init(val catalogVariant: CatalogVariant) : Inputs
        data object FetchCatalogueConfig : Inputs
        data object FetchProductVariantOptions : Inputs
        data class FetchProducts(
            val page: Int,
            val query: String,
            val categoryFilters: List<String>,
            val colorFilters: List<Color>,
            val sizeFilters: List<Size>,
            val priceFrom: String?,
            val priceTo: String?,
        ) : Inputs

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

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetProducts(val products: List<GetCatalogPageQuery.Product>) : Inputs
        data class SetPageInfo(val pageInfo: GetCatalogPageQuery.Info) : Inputs
        data class SetCatalogueConfig(val catalogueConfig: GetCatalogConfigQuery.GetCatalogConfig) : Inputs
        data class SetVariant(val catalogVariant: CatalogVariant) : Inputs
        data class SetShowBanner(val showBanner: Boolean) : Inputs
        data class SetShowSearch(val showSearch: Boolean) : Inputs
        data class SetBanner(val bannerTitle: String?, val bannerImageUrl: String?) : Inputs
        data class SetVariantOptions(val variantOptions: GetProductVariantOptions) : Inputs
        data class SetQuery(val query: String) : Inputs
        data class SetSelectedCategories(val categories: List<String>) : Inputs
        data class SetSelectedColors(val colors: List<Color>) : Inputs
        data class SetSelectedSizes(val sizes: List<Size>) : Inputs
        data class SetPriceFrom(val priceFrom: String?) : Inputs
        data class SetPriceTo(val priceTo: String?) : Inputs
        data class SetSortBy(val sortBy: ProductsSort) : Inputs
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

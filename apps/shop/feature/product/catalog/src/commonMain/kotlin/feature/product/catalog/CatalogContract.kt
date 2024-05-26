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
        val trendingNowProducts: List<GetTrendingNowProductsQuery.GetTrendingNowProduct> = emptyList(),

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

        val youAlsoViewed: List<ItemWithPrice> = listOf(
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_09e467d5-e880-4f17-8af6-9e4aa6674456.jpg?v=1493315056&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700"
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "2",
                title = "Songbird Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-2_0e3ad28e-b1a4-4940-bfd5-169df323e2bd.jpg?v=1493326475&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_b5ecc059-d675-4b8c-ada6-2d935faf0fb6.jpg?v=1493326475&width=700"
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-3_e64bbab2-8f73-47fc-93b8-1df2b79127b6.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700"
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
        ),
        val categorySection: List<CategoryItem> = listOf(
            CategoryItem(
                id = "1",
                title = "Tops",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks1.jpg?v=1614301039&width=600",
            ),
            CategoryItem(
                id = "2",
                title = "Playsuits",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks2.jpg?v=1614301039&width=600",
            ),
            CategoryItem(
                id = "3",
                title = "Dresses",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks3.jpg?v=1614301039&width=600",
            ),
        ),
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
        data class OnYouAlsoViewedItemClicked(val id: String) : Inputs
        data class OnCategoryItemClick(val id: String) : Inputs

        data class SetIsCatalogConfigLoading(val loading: Boolean) : Inputs
        data class SetProducts(val products: List<GetCatalogPageQuery.Product>) : Inputs
        data class SetTrendingProducts(val products: List<GetTrendingNowProductsQuery.GetTrendingNowProduct>) : Inputs
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

    data class ItemWithPrice(
        val id: String,
        val title: String,
        val urls: List<String>,
        val price: String,
        val sizes: String,
    )

    data class CategoryItem(val id: String, val title: String, val url: String)
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

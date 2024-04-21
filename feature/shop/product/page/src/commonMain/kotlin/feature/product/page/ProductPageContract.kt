package feature.product.page

import component.localization.Strings
import component.localization.getString
import data.AdminProductGetByIdQuery
import data.GetRecommendedProductsQuery
import data.GetSimilarProductsQuery
import data.type.BackorderStatus
import data.type.PostStatus
import data.type.StockStatus

object ProductPageContract {
    data class State(
        val isProductLoading: Boolean = true,
        val isRecommendedProductsLoading: Boolean = true,
        val isSimilarProductsLoading: Boolean = true,

        val product: AdminProductGetByIdQuery.GetProductById = AdminProductGetByIdQuery.GetProductById(
            id = "",
            name = "",
            description = "",
            tags = emptyList(),
            variants = emptyList(),
            createdAt = "",
            updatedAt = "",
            postStatus = PostStatus.Draft,
            sold = 0,
            media = listOf(),
            categoryId = null,
            isFeatured = false,
            allowReviews = false,
            creator = AdminProductGetByIdQuery.Creator(id = "", name = ""),
            pricing = AdminProductGetByIdQuery.Pricing(price = null, regularPrice = null, chargeTax = false),
            inventory = AdminProductGetByIdQuery.Inventory(
                trackQuantity = false,
                useGlobalTracking = false,
                remainingStock = 0,
                stockStatus = StockStatus.InStock,
                lowStockThreshold = 0,
                backorderStatus = BackorderStatus.Allowed
            ),
            shipping = AdminProductGetByIdQuery.Shipping(
                presetId = null,
                isPhysicalProduct = false,
                weight = null,
                length = null,
                width = null,
                height = null
            ),
            reviews = listOf(),
            totalInWishlist = 0,
        ),

        val recommendedProducts: List<GetRecommendedProductsQuery.Product> = emptyList(),
        val similarProducts: List<GetSimilarProductsQuery.Product> = emptyList(),
        val selectedMedia: AdminProductGetByIdQuery.Medium? = null,
        val selectedVariant: AdminProductGetByIdQuery.Variant? = null,
        // TODO: Add currency to the product
        val currency: Currency = Currency("£", "GBP"),
        // TODO: Add vendor to the product
        val vendor: String = "BENATTY",
        val stockStatusString: String = getString(Strings.OutOfStock),

        // TODO: Make it like so it works like. Spend 50£ more to get 5% off, then spend 100£ more to get 10% off, etc.
        val showSpendMore: Boolean = false,
        val spendMoreKey: String = "",
        val spendMoreValue: String = "",

        // TODO: Add traits to the product
        val traits: List<Trait> = listOf(Trait.EcoFriendly, Trait.Handmade),

        val colors: List<ColorItem> = emptyList(),
        val selectedColor: String? = null,

        val sizes: List<String> = emptyList(),
        val selectedSize: String? = null,
        val sizesForColor: List<String> = emptyList(),

        val currentPrice: String? = null,
        val isAddToCartButtonEnabled: Boolean = false,

        val sizeGuide: List<SizeGuide> = listOf(
            SizeGuide("XS", "6"),
            SizeGuide("S", "8"),
            SizeGuide("M", "10"),
            SizeGuide("L", "12"),
            SizeGuide("XL", "14"),
        ),

        // Ask question
        val askQuestionData: AskQuestionData = AskQuestionData(),

    )

    sealed interface Inputs {
        data class Init(val productId: String) : Inputs
        data class FetchProduct(val productId: String) : Inputs
        data object FetchSimilarProducts : Inputs
        data object FetchRecommendedProducts : Inputs

        data class OnGoToProductClicked(val productId: String) : Inputs
        data class OnGalleryMiniatureClicked(val media: AdminProductGetByIdQuery.Medium) : Inputs
        data object OnMainImageClicked : Inputs
        data class OnColorClicked(val color: String) : Inputs
        data class OnSizeClicked(val size: String) : Inputs
        data object OnAddToCartClicked : Inputs
        data object OnSizeGuideClicked : Inputs
        data object OnAskQuestionClicked : Inputs
        data class OnAskQuestionDataChanged(val data: AskQuestionData) : Inputs
        data object OnSendQuestionClicked : Inputs

        data class SetIsProductLoading(val loading: Boolean) : Inputs
        data class SetIsRecommendedProductsLoading(val loading: Boolean) : Inputs
        data class SetIsSimilarProductsLoading(val loading: Boolean) : Inputs
        data class SetProduct(val product: AdminProductGetByIdQuery.GetProductById) : Inputs
        data class SetRecommendedProducts(val products: List<GetRecommendedProductsQuery.Product>) : Inputs
        data class SetSimilarProducts(val products: List<GetSimilarProductsQuery.Product>) : Inputs
        data class SetSelectedMedia(val media: AdminProductGetByIdQuery.Medium) : Inputs
        data class SetSelectedVariant(val variant: AdminProductGetByIdQuery.Variant) : Inputs
        data class SetStockStatusString(val string: String) : Inputs
        data class SetCurrency(val currency: Currency) : Inputs
        data class SetSpendMore(val show: Boolean, val key: String, val value: String) : Inputs
        data class SetColors(val colors: List<ColorItem>) : Inputs
        data class SetSizes(val sizes: List<String>) : Inputs
        data class SetIsAddToCartButtonEnabled(val enabled: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data class GoToProduct(val productId: String) : Events
        data object OpenSizeGuideDialog : Events
        data object OpenAskQuestionDialog : Events
    }

    data class Currency(val symbol: String, val code: String)

    data class ColorItem(val value: String, val media: AdminProductGetByIdQuery.Medium1?)

    data class SizeGuide(val size: String, val uk: String)

    data class AskQuestionData(
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val question: String = "",
    )

    enum class Trait {
        Handmade,
        Organic,
        EcoFriendly,
        Vegan,
        Custom,
        Unique,
        Trending,
        Popular,
        Featured,
        Recommended,
        Special,
        Exclusive,
        Limited,
        NewArrival,
        Seasonal,
        Vintage,
        Luxury,
        Casual,
        Formal,
        BusinessCasual,
        Athletic,
        Outdoor,
        WaterResistant,
        Insulated,
        Breathable,
        Stretch,
        NonIron,
        EasyCare,
        MachineWashable,
        DryCleanOnly,
    }
}

fun ProductPageContract.Trait.titleString(): String = getString(Strings.valueOf(name))

fun ProductPageContract.Trait.descriptionString(): String = getString(Strings.valueOf("${name}TraitDescription"))

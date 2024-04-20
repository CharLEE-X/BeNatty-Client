package feature.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.Strings
import component.localization.getString
import core.models.VariantType
import data.AdminProductGetByIdQuery
import data.service.ProductService
import data.type.StockStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State>

internal class ProductPageInputHandler :
    KoinComponent,
    InputHandler<ProductPageContract.Inputs, ProductPageContract.Events, ProductPageContract.State> {

    private val productService by inject<ProductService>()

    override suspend fun InputScope.handleInput(input: ProductPageContract.Inputs) = when (input) {
        is ProductPageContract.Inputs.Init -> handleInit(input.productId)
        is ProductPageContract.Inputs.FetchProduct -> handleFetchProduct(input.productId)
        is ProductPageContract.Inputs.SetIsProductLoading -> updateState { it.copy(isProductLoading = input.loading) }
        is ProductPageContract.Inputs.SetProduct -> updateState { it.copy(product = input.product) }
        ProductPageContract.Inputs.FetchRecommendedProducts -> handleGetRecommendedProducts()
        is ProductPageContract.Inputs.SetRecommendedProducts ->
            updateState { it.copy(recommendedProducts = input.products) }

        is ProductPageContract.Inputs.OnGoToProductClicked ->
            postEvent(ProductPageContract.Events.GoToProduct(input.productId))

        ProductPageContract.Inputs.FetchSimilarProducts -> handleFetchSimilarProducts()
        is ProductPageContract.Inputs.SetSimilarProducts ->
            updateState { it.copy(similarProducts = input.products) }

        is ProductPageContract.Inputs.SetIsRecommendedProductsLoading ->
            updateState { it.copy(isRecommendedProductsLoading = input.loading) }

        is ProductPageContract.Inputs.SetIsSimilarProductsLoading ->
            updateState { it.copy(isSimilarProductsLoading = input.loading) }

        ProductPageContract.Inputs.OnMainImageClicked -> handleMainImageClicked()
        is ProductPageContract.Inputs.OnGalleryMiniatureClicked -> handleGalleryMiniatureClicked(input.media)
        is ProductPageContract.Inputs.SetSelectedMedia -> updateState { it.copy(selectedMedia = input.media) }
        is ProductPageContract.Inputs.SetStockStatusString -> updateState { it.copy(stockStatusString = input.string) }
        is ProductPageContract.Inputs.SetCurrency -> updateState { it.copy(currency = input.currency) }
        is ProductPageContract.Inputs.SetSpendMore ->
            updateState { it.copy(showSpendMore = input.show, spendMoreKey = input.key, spendMoreValue = input.value) }

        is ProductPageContract.Inputs.SetColors -> updateState { it.copy(colors = input.colors) }
        is ProductPageContract.Inputs.SetSizes -> updateState { it.copy(sizes = input.sizes) }
        is ProductPageContract.Inputs.OnColorClicked -> handleColorClicked(input.color)
        is ProductPageContract.Inputs.OnSizeClicked -> handleSizeClicked(input.size)
        is ProductPageContract.Inputs.SetSelectedVariant -> updateState {
            it.copy(
                selectedVariant = input.variant,
                currentPrice = input.variant.price.toString(),
                selectedMedia = input.variant.media.firstOrNull()?.let { media ->
                    AdminProductGetByIdQuery.Medium(
                        keyName = media.keyName,
                        url = media.url,
                        alt = media.alt,
                        type = media.type,
                    )
                }
            )
        }

        is ProductPageContract.Inputs.SetIsAddToCartButtonEnabled -> updateState { it.copy(isAddToCartButtonEnabled = input.enabled) }
        ProductPageContract.Inputs.OnAddToCartClicked -> handleOnAddToCartClicked()
    }

    private suspend fun InputScope.handleOnAddToCartClicked() {
        noOp()
        // TODO: Add item to users cart
    }

    private suspend fun InputScope.handleFetchProduct(productId: String) {
        sideJob("handleFetchProduct") {
            postInput(ProductPageContract.Inputs.SetIsProductLoading(true))
            productService.getProductById(productId).fold(
                { postEvent(ProductPageContract.Events.OnError(it.toString())) },
                {
                    postInput(ProductPageContract.Inputs.SetProduct(it.getProductById))

                    (it.getProductById.variants
                        .flatMap { it.media }
                        .map {
                            AdminProductGetByIdQuery.Medium(
                                keyName = it.keyName,
                                url = it.url,
                                alt = it.alt,
                                type = it.type,
                            )
                        } + it.getProductById.media).firstOrNull()
                        ?.let { postInput(ProductPageContract.Inputs.SetSelectedMedia(it)) }

                    it.getProductById.variants.firstOrNull()
                        ?.let {
                            postInput(ProductPageContract.Inputs.SetSelectedVariant(it))
                            // Make selection based on first variant
                            it.attrs.firstOrNull { it.key == VariantType.Color.name }?.value
                                ?.let { color -> postInput(ProductPageContract.Inputs.OnColorClicked(color)) }
                            // Enable 'add to cart' button if in stock
                            postInput(ProductPageContract.Inputs.SetIsAddToCartButtonEnabled(it.quantity > 0))
                        }

                    val stockStatusString = when (it.getProductById.inventory.stockStatus) {
                        StockStatus.InStock -> getString(Strings.InStock)
                        StockStatus.OnBackorder -> getString(Strings.OnBackorder)
                        else -> getString(Strings.OutOfStock)
                    }
                    postInput(ProductPageContract.Inputs.SetStockStatusString(stockStatusString))

                    val currency = ProductPageContract.Currency("£", "GBP")
                    postInput(ProductPageContract.Inputs.SetCurrency(currency))

                    // TODO: Remove this when the product has spend more
                    val showSpendMore = true
                    val spendMoreKey = getString(Strings.FreeShipping)
                    val spendMoreValue = "100.00"
                    postInput(ProductPageContract.Inputs.SetSpendMore(showSpendMore, spendMoreKey, spendMoreValue))

                    val allColors = it.getProductById.variants
                        .asSequence()
                        .filter { it.attrs.any { it.key == VariantType.Color.name } }
                        .map {
                            ProductPageContract.ColorItem(
                                value = it.attrs.first { it.key == VariantType.Color.name }.value,
                                media = it.media.firstOrNull(),
                            )
                        }
                        .distinctBy { it.value }
                        .toList()
                    postInput(ProductPageContract.Inputs.SetColors(allColors))

                    val allSizes = it.getProductById.variants
                        .asSequence()
                        .flatMap { it.attrs }
                        .filter { it.key == VariantType.Size.name }
                        .map { it.value }
                        .distinct()
                        .toList()
                    postInput(ProductPageContract.Inputs.SetSizes(allSizes))
                }
            )
            postInput(ProductPageContract.Inputs.SetIsProductLoading(false))
        }
    }

    private suspend fun InputScope.handleColorClicked(color: String) {
        updateState {
            val colorVariants = it.product.variants
                .filter { it.attrs.any { it.value == color } }

            // Find variant that matches color and size
            val matchedVariant = it.selectedSize?.let { size ->
                colorVariants.firstOrNull { it.attrs.any { it.value == size } }
            } ?: colorVariants.first()

            val size = matchedVariant.attrs.firstOrNull { it.key == VariantType.Size.name }?.value

            val sizesForColor = colorVariants
                .flatMap { it.attrs }
                .filter { it.key == VariantType.Size.name }
                .map { it.value }
                .distinct()

            it.copy(
                selectedColor = color,
                selectedSize = size ?: it.selectedSize,
                selectedVariant = matchedVariant,
                sizesForColor = sizesForColor,
                currentPrice = matchedVariant.price.toString(),
                selectedMedia = matchedVariant.media.firstOrNull()?.let { media ->
                    AdminProductGetByIdQuery.Medium(
                        keyName = media.keyName,
                        url = media.url,
                        alt = media.alt,
                        type = media.type,
                    )
                }
            )
        }
    }

    private suspend fun InputScope.handleSizeClicked(size: String) {
        updateState {
            val sizeVariants = it.product.variants
                .filter { it.attrs.any { it.value == size } }

            // Find variant that matches color and size
            val matchedVariant = it.selectedColor?.let { color ->
                sizeVariants.firstOrNull { it.attrs.any { it.value == color } }
            } ?: sizeVariants.first()

            it.copy(
                selectedSize = size,
                selectedVariant = matchedVariant,
                currentPrice = matchedVariant.price.toString(),
                selectedMedia = matchedVariant.media.firstOrNull()?.let { media ->
                    AdminProductGetByIdQuery.Medium(
                        keyName = media.keyName,
                        url = media.url,
                        alt = media.alt,
                        type = media.type,
                    )
                }
            )
        }
    }

    private suspend fun InputScope.handleGalleryMiniatureClicked(media: AdminProductGetByIdQuery.Medium) {
        postInput(
            ProductPageContract.Inputs.SetSelectedMedia(media)
        )
    }

    private suspend fun InputScope.handleMainImageClicked() {
        TODO("Not yet implemented. Show preview")
    }

    private suspend fun InputScope.handleFetchSimilarProducts() {
        sideJob("handleFetchSimilarProducts") {
            postInput(ProductPageContract.Inputs.SetIsSimilarProductsLoading(true))
            productService.getSimilarProducts().fold(
                { postEvent(ProductPageContract.Events.OnError(it.toString())) },
                { postInput(ProductPageContract.Inputs.SetSimilarProducts(it.getSimilarProducts.products)) }
            )
            postInput(ProductPageContract.Inputs.SetIsSimilarProductsLoading(false))
        }
    }

    private suspend fun InputScope.handleGetRecommendedProducts() {
        sideJob("handleGetRecommendedProducts") {
            postInput(ProductPageContract.Inputs.SetIsRecommendedProductsLoading(true))
            productService.getRecommendedProducts().fold(
                { postEvent(ProductPageContract.Events.OnError(it.toString())) },
                { postInput(ProductPageContract.Inputs.SetRecommendedProducts(it.getRecommendedProducts.products)) }
            )
            postInput(ProductPageContract.Inputs.SetIsRecommendedProductsLoading(false))
        }
    }

    private suspend fun InputScope.handleInit(productId: String) {
        sideJob("handleInit") {
            postInput(ProductPageContract.Inputs.FetchProduct(productId))
            postInput(ProductPageContract.Inputs.FetchSimilarProducts)
            postInput(ProductPageContract.Inputs.FetchRecommendedProducts)
        }
    }
}

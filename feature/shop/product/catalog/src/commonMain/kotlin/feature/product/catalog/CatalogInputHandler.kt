package feature.product.catalog

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.ConfigService
import data.service.ProductService
import data.type.Color
import data.type.ProductsSort
import data.type.Size
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<CatalogContract.Inputs, CatalogContract.Events, CatalogContract.State>

internal class CatalogInputHandler :
    KoinComponent,
    InputHandler<CatalogContract.Inputs, CatalogContract.Events, CatalogContract.State> {

    private val configService: ConfigService by inject()
    private val productService: ProductService by inject()

    override suspend fun InputScope.handleInput(input: CatalogContract.Inputs) = when (input) {
        is CatalogContract.Inputs.Init -> handleInit(input.catalogVariant)
        CatalogContract.Inputs.FetchCatalogueConfig -> handleFetchCatalogueConfig()
        CatalogContract.Inputs.FetchProductVariantOptions -> handleFetchProductVariantOptions()
        is CatalogContract.Inputs.FetchProducts -> handleFetchProducts(
            page = input.page,
            query = input.query,
            categoryFilters = input.categoryFilters,
            colorFilters = input.colorFilters,
            sizeFilters = input.sizeFilters,
            priceFrom = input.priceFrom,
            priceTo = input.priceTo,
        )

        is CatalogContract.Inputs.OnGoToProductClicked -> handleGoToProductClicked(input.productId)
        is CatalogContract.Inputs.OnQueryChanged -> handleOnQueryChanged(input.query)
        is CatalogContract.Inputs.OnCategoryClicked -> handleCategoryClicked(input.categoryId)
        is CatalogContract.Inputs.OnColorClicked -> handleOnColorClicked(input.color)
        is CatalogContract.Inputs.OnSizeClicked -> handleOnSizeClicked(input.size)
        is CatalogContract.Inputs.OnPriceResetClicked -> handleOnPriceResetClicked()

        is CatalogContract.Inputs.SetProducts -> updateState { it.copy(products = input.products) }
        is CatalogContract.Inputs.SetPageInfo -> updateState { it.copy(pageInfo = input.pageInfo) }
        is CatalogContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is CatalogContract.Inputs.SetCatalogueConfig -> updateState { it.copy(catalogConfig = input.catalogueConfig) }
        is CatalogContract.Inputs.SetVariant -> updateState { it.copy(catalogVariant = input.catalogVariant) }
        is CatalogContract.Inputs.SetShowBanner -> updateState { it.copy(showBanner = input.showBanner) }
        is CatalogContract.Inputs.SetShowSearch -> updateState { it.copy(showSearch = input.showSearch) }
        is CatalogContract.Inputs.SetBanner ->
            updateState { it.copy(bannerTitle = input.bannerTitle, bannerImageUrl = input.bannerImageUrl) }

        is CatalogContract.Inputs.SetVariantOptions -> updateState { it.copy(variantOptions = input.variantOptions) }
        is CatalogContract.Inputs.SetQuery -> updateState { it.copy(query = input.query) }
        is CatalogContract.Inputs.SetSelectedCategories -> updateState {
            it.copy(
                selectedCategoryIds = input.categories,
                showCategoryReset = input.categories.isNotEmpty(),
            )
        }

        is CatalogContract.Inputs.SetSelectedColors -> updateState {
            it.copy(
                selectedColors = input.colors,
                showColorReset = input.colors.isNotEmpty(),
            )
        }

        is CatalogContract.Inputs.SetSelectedSizes -> updateState {
            it.copy(
                selectedSizes = input.sizes,
                showSizeReset = input.sizes.isNotEmpty(),
            )
        }

        is CatalogContract.Inputs.SetPriceFrom -> updateState {
            it.copy(
                priceFrom = input.priceFrom,
                showPriceReset = input.priceFrom != null || it.priceTo != null,
            )
        }

        is CatalogContract.Inputs.SetPriceTo -> updateState {
            it.copy(
                priceTo = input.priceTo,
                showPriceReset = it.priceFrom != null || input.priceTo != null,
            )
        }

        CatalogContract.Inputs.OnCategoryResetClicked -> handleOnCategoryResetClicked()
        CatalogContract.Inputs.OnColorResetClicked -> handleOnColorReset()
        CatalogContract.Inputs.OnSizeResetClicked -> handleOnSizeResetClicked()
        is CatalogContract.Inputs.OnSortBySelected -> handleOnSortBySelected(input.sortBy)
        is CatalogContract.Inputs.SetSortBy -> updateState { it.copy(sortBy = input.sortBy) }
        is CatalogContract.Inputs.OnPriceFromChanged -> handleOnPriceFromChanged(input.priceFrom)
        is CatalogContract.Inputs.OnPriceToChanged -> handleOnPriceToChanged(input.priceTo)
    }

    private suspend fun InputScope.handleOnPriceToChanged(priceTo: String) {
        val state = getCurrentState()
        sideJob("onPriceToChanged") {
            postInput(CatalogContract.Inputs.SetPriceTo(priceTo))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = priceTo.ifBlank { null },
                )
            )
        }
    }

    private suspend fun InputScope.handleOnPriceFromChanged(priceFrom: String) {
        val state = getCurrentState()
        sideJob("onPriceFromChanged") {
            postInput(CatalogContract.Inputs.SetPriceFrom(priceFrom))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = priceFrom.ifBlank { null },
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnSortBySelected(sortBy: String) {
        val state = getCurrentState()
        sideJob("onSortBySelected") {
            postInput(CatalogContract.Inputs.SetSortBy(ProductsSort.valueOf(sortBy)))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnQueryChanged(query: String) {
        val state = getCurrentState()
        sideJob("onQueryChanged") {
            postInput(CatalogContract.Inputs.SetQuery(query))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnSizeResetClicked() {
        val state = getCurrentState()
        sideJob("onSizeResetClicked") {
            postInput(CatalogContract.Inputs.SetSelectedSizes(emptyList()))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = emptyList(),
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnColorReset() {
        val state = getCurrentState()
        sideJob("onColorReset") {
            postInput(CatalogContract.Inputs.SetSelectedColors(emptyList()))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = emptyList(),
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnCategoryResetClicked() {
        val state = getCurrentState()
        sideJob("onCategoryResetClicked") {
            postInput(CatalogContract.Inputs.SetSelectedCategories(emptyList()))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = emptyList(),
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnSizeClicked(size: Size) {
        val state = getCurrentState()
        sideJob("onSizeClicked") {
            val newSizes = if (size in state.selectedSizes) {
                state.selectedSizes - size
            } else {
                state.selectedSizes + size
            }
            postInput(CatalogContract.Inputs.SetSelectedSizes(newSizes))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = newSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnPriceResetClicked() {
        val state = getCurrentState()
        sideJob("handleOnResetClicked") {
            postInput(CatalogContract.Inputs.SetPriceFrom(null))
            postInput(CatalogContract.Inputs.SetPriceTo(null))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = null,
                    priceTo = null,
                )
            )
        }
    }

    private suspend fun InputScope.handleOnColorClicked(color: Color) {
        val state = getCurrentState()
        val newColors = if (color in state.selectedColors) {
            state.selectedColors - color
        } else {
            state.selectedColors + color
        }
        sideJob("onColorClicked") {
            postInput(CatalogContract.Inputs.SetSelectedColors(newColors))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = state.selectedCategoryIds,
                    colorFilters = newColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleFetchProductVariantOptions() {
        sideJob("fetchProductVariantOptions") {
            productService.getProductVariantOptions().fold(
                { postEvent(CatalogContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(CatalogContract.Inputs.SetVariantOptions(it.getProductVariantOptions)) },
            )
        }
    }

    private suspend fun InputScope.handleCategoryClicked(categoryId: String) {
        val state = getCurrentState()
        sideJob("onCategoryClicked") {
            val newCategories = if (state.selectedCategoryIds.contains(categoryId)) {
                state.selectedCategoryIds - categoryId
            } else {
                state.selectedCategoryIds + categoryId
            }
            postInput(CatalogContract.Inputs.SetSelectedCategories(newCategories))
            postInput(
                CatalogContract.Inputs.FetchProducts(
                    page = 0,
                    query = state.query,
                    categoryFilters = newCategories,
                    colorFilters = state.selectedColors,
                    sizeFilters = state.selectedSizes,
                    priceFrom = state.priceFrom,
                    priceTo = state.priceTo,
                )
            )
        }
    }

    private suspend fun InputScope.handleFetchCatalogueConfig() {
        val state = getCurrentState()
        sideJob("fetchCatalogueConfig") {
            configService.getCatalogConfig().fold(
                { postEvent(CatalogContract.Events.OnError(it.mapToUiMessage())) },
                {
                    val bannerImageUrl = with(it.getCatalogConfig.bannerConfig) {
                        when (state.catalogVariant) {
                            CatalogVariant.Catalog -> catalog.media?.url
                            CatalogVariant.Kids -> kids.media?.url
                            CatalogVariant.Men -> mens.media?.url
                            CatalogVariant.Popular -> popular.media?.url
                            CatalogVariant.Sales -> sales.media?.url
                            CatalogVariant.Women -> women.media?.url
                            is CatalogVariant.Search -> null
                        }
                    }
                    val bannerTitle = with(it.getCatalogConfig.bannerConfig) {
                        when (state.catalogVariant) {
                            CatalogVariant.Catalog,
                            is CatalogVariant.Search -> catalog.title

                            CatalogVariant.Popular -> popular.title
                            CatalogVariant.Sales -> sales.title
                            CatalogVariant.Men -> mens.title
                            CatalogVariant.Women -> women.title
                            CatalogVariant.Kids -> kids.title
                        }
                    }
                    postInput(CatalogContract.Inputs.SetBanner(bannerTitle, bannerImageUrl))
                    postInput(CatalogContract.Inputs.SetCatalogueConfig(it.getCatalogConfig))
                },
            )
        }
    }

    private suspend fun InputScope.handleFetchProducts(
        page: Int,
        query: String,
        categoryFilters: List<String>,
        colorFilters: List<Color>,
        sizeFilters: List<Size>,
        priceFrom: String?,
        priceTo: String?,
    ) {
        val state = getCurrentState()
        sideJob("fetchProducts") {
            productService.getCataloguePage(
                page = page,
                query = query.ifBlank { null },
                categories = categoryFilters.ifEmpty { null },
                colors = colorFilters.ifEmpty { null },
                sizes = sizeFilters.ifEmpty { null },
                priceFrom = priceFrom?.toDouble(),
                priceTo = priceTo?.toDouble(),
                sortBy = state.sortBy,
            ).fold(
                { postEvent(CatalogContract.Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(CatalogContract.Inputs.SetProducts(it.getCatalogPage.products))
                    postInput(CatalogContract.Inputs.SetPageInfo(it.getCatalogPage.info))
                },
            )
        }
    }
}

private suspend fun InputScope.handleInit(catalogVariant: CatalogVariant) {
    sideJob("InitCatalogue") {
        postInput(CatalogContract.Inputs.SetIsLoading(isLoading = true))
        postInput(CatalogContract.Inputs.SetVariant(catalogVariant))
        postInput(CatalogContract.Inputs.SetShowBanner(catalogVariant !is CatalogVariant.Search))
        postInput(CatalogContract.Inputs.SetShowSearch(catalogVariant is CatalogVariant.Search))
        postInput(CatalogContract.Inputs.FetchCatalogueConfig)
        postInput(
            CatalogContract.Inputs.FetchProducts(
                page = 0,
                query = "",
                categoryFilters = emptyList(),
                colorFilters = emptyList(),
                sizeFilters = emptyList(),
                priceFrom = null,
                priceTo = null,
            )
        )
        postInput(CatalogContract.Inputs.FetchProductVariantOptions)
        postInput(CatalogContract.Inputs.SetIsLoading(isLoading = false))
    }
}

private suspend fun InputScope.handleGoToProductClicked(productId: String) {
    postEvent(CatalogContract.Events.GoToProduct(productId))
}

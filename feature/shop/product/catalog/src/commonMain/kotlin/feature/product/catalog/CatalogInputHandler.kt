package feature.product.catalog

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import data.GetCatalogPageQuery
import data.service.CategoryService
import data.service.ConfigService
import data.service.ProductService
import data.type.MediaType
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<CatalogContract.Inputs, CatalogContract.Events, CatalogContract.State>

internal class CatalogInputHandler :
    KoinComponent,
    InputHandler<CatalogContract.Inputs, CatalogContract.Events, CatalogContract.State> {

    private val configService: ConfigService by inject()
    private val categoryService: CategoryService by inject()
    private val productService: ProductService by inject()

    override suspend fun InputScope.handleInput(input: CatalogContract.Inputs) = when (input) {
        is CatalogContract.Inputs.Init -> handleInit(input.variant)
        CatalogContract.Inputs.FetchCatalogueConfig -> handleFetchCatalogueConfig()
        CatalogContract.Inputs.FetchCategories -> handleFetchCategories()
        is CatalogContract.Inputs.FetchProducts -> handleFetchProducts(input.page)

        is CatalogContract.Inputs.OnGoToProductClicked -> handleGoToProductClicked(input.productId)
        is CatalogContract.Inputs.OnCategoryClicked -> handleCategoryClicked(input.categoryId)

        is CatalogContract.Inputs.SetCategories -> updateState { it.copy(categories = input.categories) }
        is CatalogContract.Inputs.SetProducts -> updateState { it.copy(products = input.products) }
        is CatalogContract.Inputs.SetPageInfo -> updateState { it.copy(pageInfo = input.pageInfo) }
        is CatalogContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is CatalogContract.Inputs.SetCatalogueConfig -> updateState { it.copy(catalogConfig = input.catalogueConfig) }
        is CatalogContract.Inputs.SetVariant -> updateState { it.copy(variant = input.variant) }
        is CatalogContract.Inputs.SetShowBanner -> updateState { it.copy(showBanner = input.showBanner) }
        is CatalogContract.Inputs.SetShowSearch -> updateState { it.copy(showSearch = input.showSearch) }
        is CatalogContract.Inputs.SetBanner ->
            updateState { it.copy(bannerTitle = input.bannerTitle, bannerImageUrl = input.bannerImageUrl) }

        is CatalogContract.Inputs.SetSelectedCategories -> updateState { it.copy(selectedCategories = input.categories) }
    }

    private suspend fun InputScope.handleCategoryClicked(categoryId: String) {
        val state = getCurrentState()
        val newCategories = if (state.selectedCategories.contains(categoryId)) {
            state.selectedCategories - categoryId
        } else {
            state.selectedCategories + categoryId
        }
        updateState { it.copy(selectedCategories = newCategories) }
        postInput(CatalogContract.Inputs.FetchProducts(page = 0))
    }

    private suspend fun InputScope.handleFetchCategories() {
        sideJob("fetchCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                onSuccess = { postInput(CatalogContract.Inputs.SetCategories(it.getCategoriesAllMinimal)) },
                onFailure = { postEvent(CatalogContract.Events.OnError(it.message ?: "Error fetching categories")) }
            )
        }
    }

    private suspend fun InputScope.handleFetchCatalogueConfig() {
        val state = getCurrentState()
        sideJob("fetchCatalogueConfig") {
            configService.getCatalogConfig().fold(
                onSuccess = { data ->
                    val bannerImageUrl = with(data.getCatalogConfig.bannerConfig) {
                        when (state.variant) {
                            Variant.Catalog -> catalog.imageUrl
                            Variant.Kids -> kids.imageUrl
                            Variant.Men -> mens.imageUrl
                            Variant.Popular -> popular.imageUrl
                            Variant.Sales -> sales.imageUrl
                            Variant.Women -> women.imageUrl
                            is Variant.Search -> null
                        }
                    }
                    val bannerTitle = with(data.getCatalogConfig.bannerConfig) {
                        when (state.variant) {
                            Variant.Catalog,
                            is Variant.Search -> catalog.title

                            Variant.Popular -> popular.title
                            Variant.Sales -> sales.title
                            Variant.Men -> mens.title
                            Variant.Women -> women.title
                            Variant.Kids -> kids.title
                        }
                    }
                    postInput(CatalogContract.Inputs.SetBanner(bannerTitle, bannerImageUrl))
                    postInput(CatalogContract.Inputs.SetCatalogueConfig(data.getCatalogConfig))
                },
                onFailure = { error ->
                    postEvent(CatalogContract.Events.OnError(error.message ?: "Error fetching catalogue config"))
                }
            )
        }
    }

    private suspend fun InputScope.handleFetchProducts(page: Int) {
        val state = getCurrentState()
//        sideJob("fetchProducts") {
//            productService.getCataloguePage(
//                page = page,
//                query = null,
//                sortBy = null,
//                categories = state.selectedCategories,
//                colors = state.selectedCategories,
//                sizes = state.selectedSizes,
//                priceFrom = state.selectedPriceFrom,
//                priceTo = state.selectedPriceTo,
//            ).fold(
//                onSuccess = { data ->
//                    postInput(CatalogContract.Inputs.SetProducts(data.getCatalogPage.products))
//                    postInput(CatalogContract.Inputs.SetPageInfo(data.getCatalogPage.info))
//                },
//                onFailure = { error ->
//                    postEvent(CatalogContract.Events.OnError(error.message ?: "Unknown error"))
//                }
//            )
//        }

        val items = (1..30).map { index ->
            GetCatalogPageQuery.Product(
                id = "$index",
                title = "Product $index",
                price = "${index}0.0",
                media = listOf(
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/1_7_800x.png?v=1663060580",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/1_7_800x.png?v=1663060580",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/3_3_800x.png?v=1663062969",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/11_5_800x.png?v=1663063289",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/6_3_800x.png?v=1663062851",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCatalogPageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/4_2.png?v=1663061599&width=1240",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                ).shuffled(),
            )
        }.shuffled()
        val products = items.map { it.copy(media = it.media.take((1..it.media.size).random())) }

        updateState {
            it.copy(
                products = products,
            )
        }
    }
}

private suspend fun InputScope.handleInit(variant: Variant) {
    sideJob("InitCatalogue") {
        postInput(CatalogContract.Inputs.SetIsLoading(isLoading = true))
        postInput(CatalogContract.Inputs.SetVariant(variant))
        postInput(CatalogContract.Inputs.SetShowBanner(variant !is Variant.Search))
        postInput(CatalogContract.Inputs.SetShowSearch(variant is Variant.Search))
        postInput(CatalogContract.Inputs.FetchCatalogueConfig)
        postInput(CatalogContract.Inputs.FetchProducts(page = 0))
        delay(5000)
        postInput(CatalogContract.Inputs.SetIsLoading(isLoading = false))
    }
}

private suspend fun InputScope.handleGoToProductClicked(productId: String) {
    postEvent(CatalogContract.Events.GoToProduct(productId))
}

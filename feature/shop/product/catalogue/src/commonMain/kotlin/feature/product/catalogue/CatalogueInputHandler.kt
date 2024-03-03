package feature.product.catalogue

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.GetCataloguePageQuery
import data.service.ConfigService
import data.service.ProductService
import data.type.MediaType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<CatalogueContract.Inputs, CatalogueContract.Events, CatalogueContract.State>

internal class CatalogueInputHandler :
    KoinComponent,
    InputHandler<CatalogueContract.Inputs, CatalogueContract.Events, CatalogueContract.State> {

    private val productService: ProductService by inject()
    private val configService: ConfigService by inject()

    override suspend fun InputScope.handleInput(input: CatalogueContract.Inputs) = when (input) {
        is CatalogueContract.Inputs.Init -> handleInit(input.variant)
        CatalogueContract.Inputs.FetchCatalogueConfig -> handleFetchCatalogueConfig()
        is CatalogueContract.Inputs.FetchProducts -> handleFetchProducts(input.page)

        is CatalogueContract.Inputs.OnGoToProductClicked -> handleGoToProductClicked(input.productId)

        is CatalogueContract.Inputs.SetProducts -> updateState { it.copy(products = input.products) }
        is CatalogueContract.Inputs.SetPageInfo -> updateState { it.copy(pageInfo = input.pageInfo) }
        is CatalogueContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is CatalogueContract.Inputs.SetCatalogueConfig -> updateState { it.copy(catalogueConfig = input.catalogueConfig) }
        is CatalogueContract.Inputs.SetVariant -> updateState { it.copy(variant = input.variant) }
        is CatalogueContract.Inputs.SetShowBanner -> updateState { it.copy(showBanner = input.showBanner) }
        is CatalogueContract.Inputs.SetShowSearch -> updateState { it.copy(showSearch = input.showSearch) }
        is CatalogueContract.Inputs.SetBanner -> updateState {
            it.copy(bannerTitle = input.bannerTitle, bannerImageUrl = input.bannerImageUrl)
        }
    }

    private suspend fun InputScope.handleFetchCatalogueConfig() {
        val state = getCurrentState()
        sideJob("fetchCatalogueConfig") {
            configService.getCatalogueConfig().fold(
                onSuccess = { data ->
                    val bannerImageUrl = with(data.getCatalogueConfig.bannerConfig) {
                        when (state.variant) {
                            Variant.Catalogue -> catalogue.imageUrl
                            Variant.Kids -> kids.imageUrl
                            Variant.Men -> mens.imageUrl
                            Variant.Popular -> popular.imageUrl
                            Variant.Sales -> sales.imageUrl
                            Variant.Women -> women.imageUrl
                            is Variant.Search -> null
                        }
                    }
                    val bannerTitle = with(data.getCatalogueConfig.bannerConfig) {
                        when (state.variant) {
                            Variant.Catalogue,
                            is Variant.Search -> catalogue.title

                            Variant.Popular -> popular.title
                            Variant.Sales -> sales.title
                            Variant.Men -> mens.title
                            Variant.Women -> women.title
                            Variant.Kids -> kids.title
                        }
                    }
                    postInput(CatalogueContract.Inputs.SetBanner(bannerTitle, bannerImageUrl))
                    postInput(CatalogueContract.Inputs.SetCatalogueConfig(data.getCatalogueConfig))
                },
                onFailure = { error ->
                    postEvent(CatalogueContract.Events.OnError(error.message ?: "Error fetching catalogue config"))
                }
            )
        }
    }

    private suspend fun InputScope.handleFetchProducts(page: Int) {
        val state = getCurrentState()
//        sideJob("fetchProducts") {
//            productService.getAsPage(
//                page = page,
//                size = state.pageSize,
//                query = null,
//                sortBy = null,
//                sortDirection = null,
//            ).fold(
//                onSuccess = { data ->
//                    postInput(CatalogueContract.Inputs.SetProducts(data.getAllProductsPage.products))
//                    postInput(CatalogueContract.Inputs.SetPageInfo(data.getAllProductsPage.info))
//                },
//                onFailure = { error ->
//                    postEvent(CatalogueContract.Events.OnError(error.message ?: "Unknown error"))
//                }
//            )
//        }


        val items = (1..30).map { index ->
            GetCataloguePageQuery.Product(
                id = "$index",
                title = "Product $index",
                price = "${index}0.0",
                media = listOf(
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/1_7_800x.png?v=1663060580",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        url = "https://minion-fashion.myshopify.com/cdn/shop/products/1_7_800x.png?v=1663060580",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        "https://minion-fashion.myshopify.com/cdn/shop/products/3_3_800x.png?v=1663062969",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        "https://minion-fashion.myshopify.com/cdn/shop/products/11_5_800x.png?v=1663063289",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        "https://minion-fashion.myshopify.com/cdn/shop/products/6_3_800x.png?v=1663062851",
                        alt = "Product $index",
                        mediaType = MediaType.Image,
                    ),
                    GetCataloguePageQuery.Medium(
                        id = "$index",
                        "https://minion-fashion.myshopify.com/cdn/shop/products/4_2.png?v=1663061599&width=1240",
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
        postInput(CatalogueContract.Inputs.SetIsLoading(isLoading = true))
        postInput(CatalogueContract.Inputs.SetVariant(variant))
        postInput(CatalogueContract.Inputs.SetShowBanner(variant !is Variant.Search))
        postInput(CatalogueContract.Inputs.SetShowSearch(variant is Variant.Search))
        postInput(CatalogueContract.Inputs.FetchCatalogueConfig)
        postInput(CatalogueContract.Inputs.FetchProducts(page = 0))
        postInput(CatalogueContract.Inputs.SetIsLoading(isLoading = false))
    }
}

private suspend fun InputScope.handleGoToProductClicked(productId: String) {
    postEvent(CatalogueContract.Events.GoToProduct(productId))
}

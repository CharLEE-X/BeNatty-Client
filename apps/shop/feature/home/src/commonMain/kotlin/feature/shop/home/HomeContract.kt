package feature.shop.home

import component.localization.Strings
import component.localization.getString
import data.GetLandingConfigQuery
import data.GetLandingConfigQuery.SlideshowItem
import data.type.MediaType

@Suppress("MaxLineLength")
object HomeContract {
    data class State(
        val isLoading: Boolean = true,

        val landingConfig: GetLandingConfigQuery.GetLandingConfig = GetLandingConfigQuery.GetLandingConfig(
            slideshowItems = listOf(),
            topCategoriesSection = GetLandingConfigQuery.TopCategoriesSection(
                left = GetLandingConfigQuery.Left(media = null, title = null),
                middle = GetLandingConfigQuery.Middle(media = null, title = null),
                right = GetLandingConfigQuery.Right(media = null, title = null)
            )

        ),
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,

        val slideshowItems: List<SlideshowItem> = listOf(
            SlideshowItem(
                id = "1",
                title = "Fine details",
                description = "Explore our collection",
                media = GetLandingConfigQuery.Media(
                    keyName = "1",
                    url = "https://be-natty.co.uk/cdn/shop/files/Dolce_Theme_Slide_1.webp?v=1717262628&width=1200",
                    alt = "Hero 1",
                    type = MediaType.Image
                ),
            ),
            SlideshowItem(
                id = "2",
                title = "Boho style",
                description = "Explore our new collection",
                media = GetLandingConfigQuery.Media(
                    keyName = "1",
                    url = "https://be-natty.co.uk/cdn/shop/files/Dolce_Theme_Slide_3.webp?v=1717262705&width=1200",
                    alt = "Hero 1",
                    type = MediaType.Image
                ),
            )
        ),
        val categorySection: List<CategoryItem> = listOf(
            CategoryItem(
                url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                title = getString(Strings.GetTops)
            ),
            CategoryItem(
                url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                title = getString(Strings.GetBottoms)
            ),
            CategoryItem(
                url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                title = getString(Strings.GetDresses)
            )
        ),
        val collections: List<ShopByCollectionItem> = listOf(
            ShopByCollectionItem(
                title = getString(Strings.Dresses),
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/Z_88c2619d-266e-45af-a71e-5152e7bdde08.jpg?v=1493324969&width=800"
            ),
            ShopByCollectionItem(
                title = getString(Strings.Tops),
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1.jpg?v=1493311371&width=800"
            ),
            ShopByCollectionItem(
                title = getString(Strings.PlaysuitsAndRompers),
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_9bb3dbf6-73ca-48ff-983e-9c1113b4e231.jpg?v=1493327316&width=800"
            ),
            ShopByCollectionItem(
                title = getString(Strings.NewArrivals),
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_09e467d5-e880-4f17-8af6-9e4aa6674456.jpg?v=1493315056&width=800"
            ),
            ShopByCollectionItem(
                title = getString(Strings.OurFavourites),
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-2_4e00cfc1-fd41-4574-ab7f-56d73620e05e.jpg?v=1493312830&width=800"
            ),
        ),
        val justArrived: List<ItemWithPrice> = listOf(
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
        ),
        val latestLooksCategories: List<LatestLookItem> = listOf(
            LatestLookItem(
                title = "Tops",
                url = "",
            ),
            LatestLookItem(
                title = "Playsuits",
                url = "",
            ),
            LatestLookItem(
                title = "Dresses",
                url = "",
            ),
        ),
        val featured: List<ItemWithPrice> = listOf(
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
        ),
        val ourFavorites: List<ItemWithPrice> = listOf(
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
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
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_5208346c-ce37-4404-b7dc-dd869cf7bc32.jpg?v=1493325201&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                ),
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "1",
                title = "Sweet Harvest Playsuit",
                urls = listOf(
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                    "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                ),
                price = "£59.00",
                sizes = "S, M, L",
            ),
        )
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchLandingConfig : Inputs
        data object FetchProducts : Inputs

        data class OnCollageItemClick(val item: SlideshowItem) : Inputs
        data object OnPrivacyPolicyClick : Inputs
        data object OnTermsOfServiceClick : Inputs
        data class OnCategoryItemClick(val id: String) : Inputs
        data class OnCollectionClicked(val title: String) : Inputs
        data class OnJustArrivedClicked(val id: String) : Inputs
        data class OnFavoriteClicked(val id: String) : Inputs
        data class OnFeaturedClicked(val id: String) : Inputs
        data object OnSeeMoreNewArrivalsClicked : Inputs
        data object OnSeeMoreFeaturedClicked : Inputs
        data object OnSeeAllFavoritesClicked : Inputs
        data class OnLatestLooksItemClick(val id: String) : Inputs

        data class OnEmailChange(val email: String) : Inputs
        data object OnEmailSend : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetLandingConfig(val landingConfig: GetLandingConfigQuery.GetLandingConfig) : Inputs
        data class SetProducts(val products: List<String>) : Inputs
        data class SetEmail(val email: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToPrivacyPolicy : Events
        data object GoToTermsOfService : Events
        data object GoToCatalogue : Events
        data class GoToProduct(val id: String) : Events
    }

    data class CategoryItem(val title: String, val url: String)
    data class ShopByCollectionItem(val title: String, val url: String)
    data class LatestLookItem(val title: String, val url: String)
    data class ItemWithPrice(
        // Also in the CatalogContract
        val id: String,
        val title: String,
        val urls: List<String>,
        val price: String,
        val sizes: String,
    )
}

data class HomeRoutes(
    val home: () -> Unit,
    val privacyPolicy: () -> Unit,
    val termsOfService: () -> Unit,
    val catalogue: () -> Unit,
    val goToProduct: (String) -> Unit,
)

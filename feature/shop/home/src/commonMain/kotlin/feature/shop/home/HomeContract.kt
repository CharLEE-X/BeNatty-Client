package feature.shop.home

import component.localization.Strings
import component.localization.getString
import data.GetLandingConfigQuery

object HomeContract {
    data class State(
        val isLoading: Boolean = true,

        val landingConfig: GetLandingConfigQuery.GetLandingConfig = GetLandingConfigQuery.GetLandingConfig(
            slideshowItems = listOf(), topCategoriesSection = GetLandingConfigQuery.TopCategoriesSection(
                left = GetLandingConfigQuery.Left(media = null, title = null),
                middle = GetLandingConfigQuery.Middle(media = null, title = null),
                right = GetLandingConfigQuery.Right(media = null, title = null)
            )

        ),
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,

        val categorySection: List<CategoryItem> = listOf(
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks1.jpg?v=1614301039&width=600",
                title = getString(Strings.GetTops)
            ),
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks2.jpg?v=1614301039&width=600",
                title = getString(Strings.GetBottoms)
            ),
            CategoryItem(
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks3.jpg?v=1614301039&width=600",
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
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_7fc77ee9-6a77-4d42-b30c-b548ecd2c34b.jpg?v=1493315057&width=700",
                price = "£59.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "2",
                title = "Songbird Playsuit",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_b5ecc059-d675-4b8c-ada6-2d935faf0fb6.jpg?v=1493326475&width=700",
                price = "£59.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "3",
                title = "Holding on Tunic",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_dfb262cc-ce27-4532-bd88-5e24b06b6522.jpg?v=1493325632&width=700",
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "4",
                title = "Fading Dreams Dress",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_09f3398d-97a9-4b12-a23a-72c879c97a6d.jpg?v=1493325201&width=700",
                price = "£89.00",
                sizes = "S, M",
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
                title = "Safari Playsuit",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_115d564a-45b6-4ea6-96c7-ca1a51c18a83.jpg?v=1493327316&width=700",
                price = "£29.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "2",
                title = "Mandala Playsuit",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_318d1d38-1e08-46c1-a44e-35777f0c7f5f.jpg?v=1493326936&width=700",
                price = "£39.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "3",
                title = "Keep in Mind Playsuit",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_a7117068-74d1-47f6-82df-e6a8c5a2e2f0.jpg?v=1493311995&width=700",
                price = "£79.00",
                sizes = "S, M",
            ),
            ItemWithPrice(
                id = "4",
                title = "Shadows Playsuit",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_720b665e-fa97-4ebb-b13d-b325f5769a69.jpg?v=1493325769&width=700",
                price = "£89.00",
                sizes = "S, M, L",
            ),
        ),
        val ourFavorites: List<ItemWithPrice> = listOf(
            ItemWithPrice(
                id = "1",
                title = "Coco Top",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1_6ce1d988-f31b-4b91-8e8a-1776f2f96ca0.jpg?v=1493312830&width=700",
                price = "£39.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "2",
                title = "Breezie Tie Top",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/9k_-1_e6c12dd9-d404-4539-a200-b6183279c29f.jpg?v=1493314873&width=700",
                price = "£59.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "3",
                title = "Coco Shirt",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-2_3453b5dc-2191-42e6-9f53-ad79972ee920.jpg?v=1493312715&width=1200",
                price = "£79.00",
                sizes = "S, M, L",
            ),
            ItemWithPrice(
                id = "4",
                title = "Be True Blouse",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/products/2Q_-1.jpg?v=1493311371&width=700",
                price = "£89.00",
                sizes = "S, M",
            ),
        )
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchLandingConfig : Inputs
        data object FetchProducts : Inputs

        data class OnCollageItemClick(val item: GetLandingConfigQuery.SlideshowItem) : Inputs
        data object OnPrivacyPolicyClick : Inputs
        data object OnTermsOfServiceClick : Inputs
        data object OnBannerLeftClick : Inputs
        data object OnBannerRightClick : Inputs
        data class OnCollectionClicked(val title: String) : Inputs
        data class OnJustArrivedClicked(val id: String) : Inputs
        data class OnFeaturedClicked(val id: String) : Inputs
        data object OnOnSeeMoreNewArrivalsClicked : Inputs
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
        val id: String,
        val title: String,
        val url: String,
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

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
        val featuredCollections: Section<ShopByCollectionItem> = Section(
            title = "Featured collections",
            (1..5).map {
                ShopByCollectionItem(
                    title = getString(Strings.Dresses),
                    url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700"
                )
            },
        ),
        val latestLooks: String = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
        val justArrived: Section<ItemWithPrice> = Section(
            title = "Just arrived",
            (1..5).map {
                ItemWithPrice(
                    id = "1",
                    title = "Sweet Harvest Playsuit",
                    urls = listOf(
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700"
                    ),
                    price = "£59.00",
                    sizes = "S, M, L",
                )
            },
        ),
        val latestLooksCategories: Section<LatestLookItem> = Section(
            title = "Latest looks",
            items = (1..5).map {
                LatestLookItem(
                    title = "Tops",
                    url = "",
                )
            },
        ),
        val featured: Section<ItemWithPrice> = Section(
            title = "Featured",
            items = (1..5).map {
                ItemWithPrice(
                    id = "1",
                    title = "Sweet Harvest Playsuit",
                    urls = listOf(
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700"
                    ),
                    price = "£59.00",
                    sizes = "S, M, L",
                )
            }
        ),
        val ourFavorites: Section<ItemWithPrice> = Section(
            title = "Our favorites",
            items = (1..5).map {
                ItemWithPrice(
                    id = "1",
                    title = "Sweet Harvest Playsuit",
                    urls = listOf(
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                        "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700"
                    ),
                    price = "£59.00",
                    sizes = "S, M, L",
                )
            }
        ),
        val fromTheBlog: Section<BlogItem> = Section(
            title = "From the blog",
            items = listOf(
                BlogItem(
                    title = "Color pop",
                    description = "The best way to add a pop of color to your wardrobe",
                    url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                    date = "June 1, 2021",
                    links = listOf(
                        BlogItem.Link(
                            title = "Clothing",
                            url = ""
                        ),
                        BlogItem.Link(
                            title = "Fashion",
                            url = ""
                        )
                    )
                ),
                BlogItem(
                    title = "Summer look book",
                    description = "The best way to add a pop of color to your wardrobe",
                    url = "https://be-natty.co.uk/cdn/shop/files/DolceThemePreview_1.webp?v=1717228009&width=700",
                    date = "June 1, 2021",
                    links = listOf(
                        BlogItem.Link(
                            title = "Clothing",
                            url = ""
                        ),
                        BlogItem.Link(
                            title = "Fashion",
                            url = ""
                        )
                    )
                )
            ),
        ),
        val freeSection: List<FreeItem> = listOf(
            FreeItem(
                title = "Free shipping",
                description = "On all orders over $100",
                icon = "MdiLocalShipping"
            ),
            FreeItem(
                title = "Free returns",
                description = "On all unopened unused items",
                icon = "MdiRefresh"
            ),
            FreeItem(
                title = "Secure shipping",
                description = "Shop with confidence",
                icon = "MdiVerifiedUser"
            ),
            FreeItem(
                title = "Percent satisfaction",
                description = "Rated excellent by customers",
                icon = "MdiSentimentSatisfied"
            )
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

    data class Section<T>(
        val title: String,
        val items: List<T>,
    )

    data class BlogItem(
        val title: String,
        val description: String,
        val url: String,
        val date: String,
        val links: List<Link>,
    ) {
        data class Link(
            val title: String,
            val url: String,
        )
    }

    data class FreeItem(
        val title: String,
        val description: String,
        val icon: String,
    )
}

data class HomeRoutes(
    val home: () -> Unit,
    val privacyPolicy: () -> Unit,
    val termsOfService: () -> Unit,
    val catalogue: () -> Unit,
    val goToProduct: (String) -> Unit,
)

package feature.admin.product.page

import component.localization.getString
import data.GetCategoriesAllMinimalQuery
import data.GetCategoryByIdQuery
import data.ProductGetByIdQuery
import data.TagsGetAllMinimalQuery
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.StockStatus

object AdminProductPageContract {
    data class State(
        val isLoading: Boolean = false,
        val isImagesLoading: Boolean = false,
        val screenState: ScreenState,

        val wasEdited: Boolean = false,

        val nameError: String? = null,
        val shakeName: Boolean = false,
        val shortDescriptionError: String? = null,
        val shakeShortDescription: Boolean = false,
        val descriptionError: String? = null,
        val shakeDescription: Boolean = false,
        val lowStockThresholdError: String? = null,
        val shakeLowStockThreshold: Boolean = false,
        val remainingStockError: String? = null,
        val shakeRemainingStock: Boolean = false,
        val priceError: String? = null,
        val shakePrice: Boolean = false,
        val regularPriceError: String? = null,
        val shakeRegularPrice: Boolean = false,
        val salePriceError: String? = null,
        val shakeSalePrice: Boolean = false,
        val weightError: String? = null,
        val shakeWeight: Boolean = false,
        val heightError: String? = null,
        val shakeHeight: Boolean = false,
        val lengthError: String? = null,
        val shakeLength: Boolean = false,
        val widthError: String? = null,
        val shakeWidth: Boolean = false,
        val imageDropError: String? = null,

        val isCreateDisabled: Boolean = true,
        val allCategories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal> = emptyList(),
        val allTags: List<TagsGetAllMinimalQuery.GetTagsAllMinimal> = emptyList(),
        val presetCategory: GetCategoryByIdQuery.GetCategoryById? = null,

        val original: ProductGetByIdQuery.GetProductById = ProductGetByIdQuery.GetProductById(
            id = "",
            common = ProductGetByIdQuery.Common(
                name = "",
                shortDescription = "",
                allowReviews = true,
                catalogVisibility = CatalogVisibility.Everywhere,
                categories = emptyList(),
                isFeatured = false,
                relatedIds = emptyList(),
                tags = emptyList(),
                createdBy = "",
                createdAt = "",
                updatedAt = "",
            ),
            data = ProductGetByIdQuery.Data1(
                postStatus = PostStatus.Draft,
                description = "",
                isPurchasable = false,
                images = emptyList(),
                parentId = null,
            ),
            inventory = ProductGetByIdQuery.Inventory(
                onePerOrder = false,
                backorderStatus = BackorderStatus.Allowed,
                canBackorder = true,
                isOnBackorder = true,
                lowStockThreshold = 0,
                remainingStock = 0,
                stockStatus = StockStatus.OutOfStock,
                trackInventory = true,
            ),
            price = ProductGetByIdQuery.Price(
                price = null,
                regularPrice = null,
                salePrice = null,
                onSale = false,
                saleStart = null,
                saleEnd = null,
            ),
            shipping = ProductGetByIdQuery.Shipping(
                presetId = null,
                height = null,
                length = null,
                weight = null,
                width = null,
                requiresShipping = true,
            ),
            creator = ProductGetByIdQuery.Creator(
                id = "",
                name = "",
            ),
            reviews = emptyList(),
            totalInWishlist = 0,
        ),

        val current: ProductGetByIdQuery.GetProductById = original,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val productId: String?) : Inputs
        data class UploadImage(val imageString: String) : Inputs

        sealed interface Get : Inputs {
            data class ProductById(val id: String) : Inputs
            data object AllCategories : Inputs
            data object AllTags : Inputs
            data class PresetCategory(val categoryId: String) : Inputs
        }

        sealed interface OnClick : Inputs {
            data object Create : OnClick
            data object Delete : OnClick
            data object SaveEdit : Inputs
            data object CancelEdit : OnClick
            data class CategorySelected(val categoryName: String) : Inputs
            data class TagSelected(val tagName: String) : Inputs
            data object GoToCreateCategory : Inputs
            data object GoToCreateTag : Inputs
            data object GoToUserCreator : Inputs
            data class PresetSelected(val preset: String) : Inputs
            data object ImproveName : Inputs
            data object ImproveShortDescription : Inputs
            data object ImproveDescription : Inputs
            data object ImproveTags : Inputs
            data class DeleteImage(val imageId: String) : Inputs
        }

        sealed interface Set : Inputs {
            data class AllCategories(val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal>) :
                Inputs

            data class AllTags(val tags: List<TagsGetAllMinimalQuery.GetTagsAllMinimal>) : Inputs

            data class Loading(val isLoading: Boolean) : Inputs
            data class ImagesLoading(val isImagesLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs
            data class OriginalProduct(val product: ProductGetByIdQuery.GetProductById) : Inputs
            data class CurrentProduct(val product: ProductGetByIdQuery.GetProductById) : Inputs

            data class Id(val id: String) : Inputs
            data class Name(val name: String) : Inputs
            data class NameShake(val shake: Boolean) : Inputs
            data class ShortDescription(val shortDescription: String) : Inputs
            data class ShortDescriptionShake(val shake: Boolean) : Inputs
            data class IsFeatured(val isFeatured: Boolean) : Inputs
            data class AllowReviews(val allowReviews: Boolean) : Inputs
            data class VisibilityInCatalog(val catalogVisibility: CatalogVisibility) : Inputs
            data class Creator(val creator: ProductGetByIdQuery.Creator) : Inputs
            data class CreatedAt(val createdAt: String) : Inputs
            data class UpdatedAt(val updatedAt: String) : Inputs
            data class Categories(val categories: List<String>) : Inputs
            data class StatusOfPost(val postStatus: PostStatus) : Inputs
            data class Description(val description: String) : Inputs
            data class DescriptionShake(val shake: Boolean) : Inputs
            data class IsPurchasable(val isPurchasable: Boolean) : Inputs
            data class OnePerOrder(val onePerOrder: Boolean) : Inputs
            data class StatusOfBackorder(val backorderStatus: BackorderStatus) : Inputs
            data class CanBackorder(val canBackorder: Boolean) : Inputs
            data class IsOnBackorder(val isOnBackorder: Boolean) : Inputs
            data class LowStockThreshold(val lowStockThreshold: Int) : Inputs
            data class LowStockThresholdShake(val shake: Boolean) : Inputs
            data class RemainingStock(val remainingStock: Int) : Inputs
            data class RemainingStockShake(val shake: Boolean) : Inputs
            data class StatusOfStock(val stockStatus: StockStatus) : Inputs
            data class TrackInventory(val trackInventory: Boolean) : Inputs
            data class Price(val price: String) : Inputs
            data class PriceShake(val shake: Boolean) : Inputs
            data class RegularPrice(val regularPrice: String) : Inputs
            data class RegularPriceShake(val shake: Boolean) : Inputs
            data class SalePrice(val salePrice: String) : Inputs
            data class SalePriceShake(val shake: Boolean) : Inputs
            data class OnSale(val onSale: Boolean) : Inputs
            data class SaleStart(val saleStart: String) : Inputs
            data class SaleEnd(val saleEnd: String) : Inputs
            data class Height(val height: String) : Inputs
            data class HeightShake(val shake: Boolean) : Inputs
            data class Length(val length: String) : Inputs
            data class LengthShake(val shake: Boolean) : Inputs
            data class Weight(val weight: String) : Inputs
            data class WeightShake(val shake: Boolean) : Inputs
            data class Width(val width: String) : Inputs
            data class WidthShake(val shake: Boolean) : Inputs
            data class RequiresShipping(val requiresShipping: Boolean) : Inputs
            data class Images(val images: List<ProductGetByIdQuery.Image>) : Inputs
            data class PresetCategory(val category: GetCategoryByIdQuery.GetCategoryById?) : Inputs
            data class ShippingPresetId(val presetId: String?) : Inputs
            data class ImageDropError(val error: String?) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToProductList : Events
        data class GoToUserDetails(val userId: String) : Events
        data object GoToCreateCategory : Events
        data object GoToCreateTag : Events
        data class GoToProduct(val id: String) : Events
    }

    data class Strings(
        val products: String = getString(component.localization.Strings.Products),
        val country: String = getString(component.localization.Strings.Country),
        val save: String = getString(component.localization.Strings.Save),
        val cancel: String = getString(component.localization.Strings.Cancel),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
        val never: String = getString(component.localization.Strings.Never),
        val details: String = getString(component.localization.Strings.Details),
        val name: String = getString(component.localization.Strings.Name),
        val create: String = getString(component.localization.Strings.Create),
        val productShortDescription: String = getString(component.localization.Strings.ProductShortDescription),
        val isFeatured: String = getString(component.localization.Strings.IsFeatured),
        val isFeaturedDesc: String = getString(component.localization.Strings.IsFeaturedDesc),
        val allowReviews: String = getString(component.localization.Strings.AllowReviews),
        val allowReviewsDesc: String = getString(component.localization.Strings.AllowReviewsDesc),
        val catalogVisibility: String = getString(component.localization.Strings.CatalogVisibility),
        val createProduct: String = getString(component.localization.Strings.CreateProduct),
        val product: String = getString(component.localization.Strings.Product),
        val categories: String = getString(component.localization.Strings.Categories),
        val tags: String = getString(component.localization.Strings.Tags),
        val postStatus: String = getString(component.localization.Strings.PostStatus),
        val data: String = getString(component.localization.Strings.Data),
        val description: String = getString(component.localization.Strings.Description),
        val isPurchasable: String = getString(component.localization.Strings.IsPurchasable),
        val inventory: String = getString(component.localization.Strings.Inventory),
        val onePerOrder: String = getString(component.localization.Strings.OnePerOrder),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val reset: String = getString(component.localization.Strings.Reset),
        val images: String = getString(component.localization.Strings.Images),
        val noImages: String = getString(component.localization.Strings.NoImages),
        val backorderStatus: String = getString(component.localization.Strings.BackorderStatus),
        val canBackorder: String = getString(component.localization.Strings.CanBackorder),
        val isOnBackorder: String = getString(component.localization.Strings.IsOnBackorder),
        val lowStockThreshold: String = getString(component.localization.Strings.LowStockThreshold),
        val remainingStock: String = getString(component.localization.Strings.RemainingStock),
        val stockStatus: String = getString(component.localization.Strings.StockStatus),
        val trackInventory: String = getString(component.localization.Strings.TrackInventory),
        val price: String = getString(component.localization.Strings.Price),
        val regularPrice: String = getString(component.localization.Strings.RegularPrice),
        val salePrice: String = getString(component.localization.Strings.SalePrice),
        val onSale: String = getString(component.localization.Strings.OnSale),
        val saleStart: String = getString(component.localization.Strings.SaleStart),
        val saleEnd: String = getString(component.localization.Strings.SaleEnd),
        val shipping: String = getString(component.localization.Strings.Shipping),
        val height: String = getString(component.localization.Strings.Height),
        val length: String = getString(component.localization.Strings.Length),
        val weight: String = getString(component.localization.Strings.Weight),
        val width: String = getString(component.localization.Strings.Width),
        val requiresShipping: String = getString(component.localization.Strings.RequiresShipping),
        val noTags: String = getString(component.localization.Strings.NoTags),
        val shippingPreset: String = getString(component.localization.Strings.ShippingPreset),
        val noOtherCategoriesToChooseFrom: String = getString(component.localization.Strings.NoOtherCategoriesToChooseFrom),
        val createCategory: String = getString(component.localization.Strings.CreateCategory),
        val createTag: String = getString(component.localization.Strings.CreateTag),
        val catalogVisibilityDesc: String = getString(component.localization.Strings.CatalogVisibilityDesc),
        val categoriesDesc: String = getString(component.localization.Strings.CategoriesDesc),
        val tagsDesc: String = getString(component.localization.Strings.TagsDesc),
        val createdByDesc: String = getString(component.localization.Strings.CreatedByDesc),
        val postStatusDesc: String = getString(component.localization.Strings.PostStatusDesc),
        val onePerOrderDesc: String = getString(component.localization.Strings.OnePerOrderDesc),
        val isPurchasableDesc: String = getString(component.localization.Strings.IsPurchasableDesc),
        val backorderStatusDesc: String = getString(component.localization.Strings.BackorderStatusDesc),
        val canBackorderDesc: String = getString(component.localization.Strings.CanBackorderDesc),
        val isOnBackorderDesc: String = getString(component.localization.Strings.IsOnBackorderDesc),
        val lowStockThresholdDesc: String = getString(component.localization.Strings.LowStockThresholdDesc),
        val remainingStockDesc: String = getString(component.localization.Strings.RemainingStockDesc),
        val descriptionDesc: String = getString(component.localization.Strings.DescriptionDesc),
        val productShortDescriptionDesc: String = getString(component.localization.Strings.ProductShortDescriptionDesc),
        val stockStatusDesc: String = getString(component.localization.Strings.StockStatusDesc),
        val trackInventoryDesc: String = getString(component.localization.Strings.TrackInventoryDesc),
        val imagesDesc: String = getString(component.localization.Strings.ImagesDesc),
        val priceDesc: String = getString(component.localization.Strings.PriceDesc),
        val regularPriceDesc: String = getString(component.localization.Strings.RegularPriceDesc),
        val salePriceDesc: String = getString(component.localization.Strings.SalePriceDesc),
        val onSaleDesc: String = getString(component.localization.Strings.OnSaleDesc),
        val saleEndDesc: String = getString(component.localization.Strings.SaleEndDesc),
        val saleStartDesc: String = getString(component.localization.Strings.SaleStartDesc),
        val weightDesc: String = getString(component.localization.Strings.WeightDesc),
        val lengthDesc: String = getString(component.localization.Strings.LengthDesc),
        val widthDesc: String = getString(component.localization.Strings.WidthDesc),
        val heightDesc: String = getString(component.localization.Strings.HeightDesc),
        val shippingPresetDesc: String = getString(component.localization.Strings.ShippingPresetDesc),
        val improveWithAi: String = getString(component.localization.Strings.ImproveWithAi),
        val addImage: String = getString(component.localization.Strings.AddImage),
        val deleteExplain: String = getString(component.localization.Strings.DeleteExplain),
    ) {
    }

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}

package feature.admin.product.page

import component.localization.getString
import data.AdminProductGetByIdQuery
import data.GetCategoriesAllMinimalQuery
import data.GetCategoryByIdQuery
import data.TagsGetAllMinimalQuery
import data.type.BackorderStatus
import data.type.PostStatus
import data.type.StockStatus

object AdminProductPageContract {
    data class State(
        val strings: Strings = Strings(),
        val original: AdminProductGetByIdQuery.GetAdminProductById? = null,
        val current: AdminProductGetByIdQuery.GetAdminProductById? = original,

        val isLoading: Boolean = false,
        val isImagesLoading: Boolean = false,
        val screenState: ScreenState,

        val localMedia: List<AdminProductGetByIdQuery.Medium> = emptyList(),

        val wasEdited: Boolean = false,

        val titleError: String? = null,
        val descriptionError: String? = null,
        val lowStockThresholdError: String? = null,
        val remainingStockError: String? = null,
        val priceError: String? = null,
        val regularPriceError: String? = null,
        val weightError: String? = null,
        val heightError: String? = null,
        val lengthError: String? = null,
        val widthError: String? = null,
        val imageDropError: String? = null,

        val isCreateDisabled: Boolean = true,
        val allCategories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal> = emptyList(),
        val allTags: List<TagsGetAllMinimalQuery.GetTagsAllMinimal> = emptyList(),
        val presetCategory: GetCategoryByIdQuery.GetCategoryById? = null,
    )

    sealed interface Inputs {
        data class Init(val productId: String?) : Inputs
        data class UploadMedia(val mediaString: String) : Inputs
        data class AddMedia(val mediaString: String) : Inputs
        data class DeleteMedia(val index: Int) : Inputs

        data class GetProductById(val id: String) : Inputs
        data object GetAllCategories : Inputs
        data object GetAllTags : Inputs
        data class GetPresetCategory(val categoryId: String) : Inputs

        data object OnDeleteClick : Inputs
        data object OnSaveClick : Inputs
        data object OnDiscardClick : Inputs
        data object OnCreateCategoryClick : Inputs
        data object OnCreateTagClick : Inputs
        data object OnUserCreatorClick : Inputs
        data class OnCategorySelected(val categoryName: String) : Inputs
        data class OnTagSelected(val tagName: String) : Inputs
        data class OnPresetSelected(val preset: String) : Inputs
        data class OnDeleteImageClick(val imageId: String) : Inputs

        data class SetAllCategories(val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal>) :
            Inputs

        data class SetAllTags(val tags: List<TagsGetAllMinimalQuery.GetTagsAllMinimal>) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetImagesLoading(val isImagesLoading: Boolean) : Inputs
        data class SetStateOfScreen(val screenState: ScreenState) : Inputs
        data class SetOriginalProduct(val product: AdminProductGetByIdQuery.GetAdminProductById) : Inputs
        data class SetCurrentProduct(val product: AdminProductGetByIdQuery.GetAdminProductById) : Inputs
        data class SetLocalMedia(val media: List<AdminProductGetByIdQuery.Medium>) : Inputs

        data class SetId(val id: String) : Inputs
        data class SetTitle(val title: String) : Inputs
        data class SetDescription(val description: String) : Inputs
        data class SetStatusOfPost(val postStatus: PostStatus) : Inputs
        data class SetMedia(val media: List<AdminProductGetByIdQuery.Medium>) : Inputs
        data class SetImageDropError(val error: String?) : Inputs
        data class SetCategoryId(val categoryId: String) : Inputs
        data class SetIsFeatured(val isFeatured: Boolean) : Inputs
        data class SetAllowReviews(val allowReviews: Boolean) : Inputs
        data class SetCreator(val creator: AdminProductGetByIdQuery.Creator) : Inputs
        data class SetCreatedAt(val createdAt: String) : Inputs
        data class SetUpdatedAt(val updatedAt: String) : Inputs

        // Pricing
        data class SetPrice(val price: String) : Inputs
        data class SetRegularPrice(val regularPrice: String) : Inputs
        data class SetChargeTax(val chargeTax: Boolean) : Inputs

        // Inventory
        data class SetTrackQuantity(val trackQuantity: Boolean) : Inputs
        data class SetUseGlobalTracking(val useGlobalTracking: Boolean) : Inputs
        data class SetRemainingStock(val remainingStock: Int) : Inputs
        data class SetStatusOfStock(val stockStatus: StockStatus) : Inputs
        data class SetLowStockThreshold(val lowStockThreshold: Int) : Inputs
        data class SetStatusOfBackorder(val backorderStatus: BackorderStatus) : Inputs

        // Shipping
        data class SetShippingPresetId(val presetId: String?) : Inputs
        data class SetPresetCategory(val category: GetCategoryByIdQuery.GetCategoryById?) : Inputs
        data class SetIsPhysicalProduct(val isPhysicalProduct: Boolean) : Inputs
        data class SetHeight(val height: String) : Inputs
        data class SetLength(val length: String) : Inputs
        data class SetWeight(val weight: String) : Inputs
        data class SetWidth(val width: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBackToProducts : Events
        data class GoToUserDetails(val userId: String) : Events
        data object GoToCreateCategory : Events
        data object GoToCreateTag : Events
        data class GoToProduct(val id: String) : Events
    }

    data class Strings(
        val products: String = getString(component.localization.Strings.Products),
        val country: String = getString(component.localization.Strings.Country),
        val save: String = getString(component.localization.Strings.Save),
        val discard: String = getString(component.localization.Strings.Discard),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
        val never: String = getString(component.localization.Strings.Never),
        val details: String = getString(component.localization.Strings.Details),
        val title: String = getString(component.localization.Strings.Title),
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
        val status: String = getString(component.localization.Strings.Status),
        val data: String = getString(component.localization.Strings.Data),
        val description: String = getString(component.localization.Strings.Description),
        val isPurchasable: String = getString(component.localization.Strings.IsPurchasable),
        val inventory: String = getString(component.localization.Strings.Inventory),
        val onePerOrder: String = getString(component.localization.Strings.OnePerOrder),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val dismiss: String = getString(component.localization.Strings.Dismiss),
        val images: String = getString(component.localization.Strings.Images),
        val noImages: String = getString(component.localization.Strings.NoImages),
        val backorderStatus: String = getString(component.localization.Strings.BackorderStatus),
        val canBackorder: String = getString(component.localization.Strings.CanBackorder),
        val isOnBackorder: String = getString(component.localization.Strings.IsOnBackorder),
        val lowStockThreshold: String = getString(component.localization.Strings.LowStockThreshold),
        val remainingStock: String = getString(component.localization.Strings.RemainingStock),
        val stockStatus: String = getString(component.localization.Strings.StockStatus),
        val trackQuantity: String = getString(component.localization.Strings.TrackQuantity),
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
        val isPhysicalProduct: String = getString(component.localization.Strings.IsPhysicalProduct),
        val noTags: String = getString(component.localization.Strings.NoTags),
        val shippingPreset: String = getString(component.localization.Strings.ShippingPreset),
        val noCategories: String = getString(component.localization.Strings.NoCategories),
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
        val stockStatusDesc: String = getString(component.localization.Strings.StockStatusDesc),
        val trackQuantityDesc: String = getString(component.localization.Strings.TrackQuantityDesc),
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
        val chargeTax: String = getString(component.localization.Strings.ChargeTax),
        val chargeTaxDesc: String = getString(component.localization.Strings.ChargeTaxDesc),
        val useGlobalTracking: String = getString(component.localization.Strings.UseGlobalTracking),
        val useGlobalTrackingDesc: String = getString(component.localization.Strings.UseGlobalTrackingDesc),
        val media: String = getString(component.localization.Strings.Media),
        val info: String = getString(component.localization.Strings.Info),
        val insights: String = getString(component.localization.Strings.Insights),
        val noInsights: String = getString(component.localization.Strings.NoInsights),
        val productOrganization: String = getString(component.localization.Strings.ProductOrganization),
    ) {
    }

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}

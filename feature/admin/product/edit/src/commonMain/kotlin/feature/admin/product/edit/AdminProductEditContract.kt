package feature.admin.product.edit

import component.localization.getString
import data.AdminProductGetByIdQuery
import data.GetAllCategoriesAsMinimalQuery
import data.GetCategoryByIdQuery
import data.TagsGetAllMinimalQuery
import data.type.BackorderStatus
import data.type.PostStatus
import data.type.StockStatus

object AdminProductEditContract {
    data class State(
        val isLoading: Boolean = false,
        val isImagesLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val localMedia: List<AdminProductGetByIdQuery.Medium> = emptyList(),

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

        val allCategories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal> = emptyList(),
        val allTags: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal> = emptyList(),
        val presetCategory: GetCategoryByIdQuery.GetCategoryById? = null,

        val showAddVariantFields: Boolean = false,

        val original: AdminProductGetByIdQuery.GetProductById = AdminProductGetByIdQuery.GetProductById(
            id = "",
            name = "",
            description = "",
            postStatus = PostStatus.Active,
            media = emptyList(),
            categoryId = null,
            tags = emptyList(),
            allowReviews = true,
            isFeatured = false,
            creator = AdminProductGetByIdQuery.Creator(
                id = "",
                name = "",
            ),
            createdAt = "",
            updatedAt = "",
            pricing = AdminProductGetByIdQuery.Pricing(
                price = null,
                regularPrice = null,
                chargeTax = true,
            ),
            inventory = AdminProductGetByIdQuery.Inventory(
                trackQuantity = true,
                useGlobalTracking = true,
                backorderStatus = BackorderStatus.Allowed,
                lowStockThreshold = 0,
                remainingStock = 0,
                stockStatus = StockStatus.OutOfStock,
            ),
            shipping = AdminProductGetByIdQuery.Shipping(
                presetId = null,
                isPhysicalProduct = true,
                height = null,
                length = null,
                weight = null,
                width = null,
            ),
            reviews = emptyList(),
            totalInWishlist = 0,
            sold = 0,
            variants = emptyList(),
        ),

        val current: AdminProductGetByIdQuery.GetProductById = original,
    )

    sealed interface Inputs {
        data class Init(val productId: String) : Inputs
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
        data object OnCreateVariantClick : Inputs

        data class SetAllCategories(val categories: List<GetAllCategoriesAsMinimalQuery.GetAllCategoriesAsMinimal>) :
            Inputs

        data class SetAllTags(val tags: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal>) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetImagesLoading(val isImagesLoading: Boolean) : Inputs
        data class SetOriginal(val product: AdminProductGetByIdQuery.GetProductById) : Inputs
        data class SetCurrent(val product: AdminProductGetByIdQuery.GetProductById) : Inputs
        data class SetLocalMedia(val media: List<AdminProductGetByIdQuery.Medium>) : Inputs

        data class SetId(val id: String) : Inputs
        data class SetName(val title: String) : Inputs
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
}

val adminProductPageStrings = AdminProductPageStrings

object AdminProductPageStrings {
    val products: String = getString(component.localization.Strings.Products)
    val save: String = getString(component.localization.Strings.Save)
    val discard: String = getString(component.localization.Strings.Discard)
    val createdBy: String = getString(component.localization.Strings.CreatedBy)
    val delete: String = getString(component.localization.Strings.Delete)
    val createdAt: String = getString(component.localization.Strings.CreatedAt)
    val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt)
    val name: String = getString(component.localization.Strings.Title)
    val create: String = getString(component.localization.Strings.Create)
    val isFeatured: String = getString(component.localization.Strings.IsFeatured)
    val isFeaturedDesc: String = getString(component.localization.Strings.IsFeaturedDesc)
    val allowReviews: String = getString(component.localization.Strings.AllowReviews)
    val allowReviewsDesc: String = getString(component.localization.Strings.AllowReviewsDesc)
    val product: String = getString(component.localization.Strings.Product)
    val categories: String = getString(component.localization.Strings.Categories)
    val tags: String = getString(component.localization.Strings.Tags)
    val status: String = getString(component.localization.Strings.Status)
    val data: String = getString(component.localization.Strings.Data)
    val description: String = getString(component.localization.Strings.Description)
    val inventory: String = getString(component.localization.Strings.Inventory)
    val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges)
    val saveChanges: String = getString(component.localization.Strings.SaveChanges)
    val dismiss: String = getString(component.localization.Strings.Dismiss)
    val backorderStatus: String = getString(component.localization.Strings.BackorderStatus)
    val lowStockThreshold: String = getString(component.localization.Strings.LowStockThreshold)
    val remainingStock: String = getString(component.localization.Strings.RemainingStock)
    val stockStatus: String = getString(component.localization.Strings.StockStatus)
    val trackQuantity: String = getString(component.localization.Strings.TrackQuantity)
    val price: String = getString(component.localization.Strings.Price)
    val regularPrice: String = getString(component.localization.Strings.RegularPrice)
    val shipping: String = getString(component.localization.Strings.Shipping)
    val height: String = getString(component.localization.Strings.Height)
    val length: String = getString(component.localization.Strings.Length)
    val weight: String = getString(component.localization.Strings.Weight)
    val width: String = getString(component.localization.Strings.Width)
    val noTags: String = getString(component.localization.Strings.NoTags)
    val shippingPreset: String = getString(component.localization.Strings.ShippingPreset)
    val noCategories: String = getString(component.localization.Strings.NoCategories)
    val createCategory: String = getString(component.localization.Strings.CreateCategory)
    val createTag: String = getString(component.localization.Strings.CreateTag)
    val categoriesDesc: String = getString(component.localization.Strings.CategoriesDesc)
    val tagsDesc: String = getString(component.localization.Strings.TagsDesc)
    val createdByDesc: String = getString(component.localization.Strings.CreatedByDesc)
    val postStatusDesc: String = getString(component.localization.Strings.PostStatusDesc)
    val backorderStatusDesc: String = getString(component.localization.Strings.BackorderStatusDesc)
    val lowStockThresholdDesc: String = getString(component.localization.Strings.LowStockThresholdDesc)
    val remainingStockDesc: String = getString(component.localization.Strings.RemainingStockDesc)
    val descriptionDesc: String = getString(component.localization.Strings.DescriptionDesc)
    val stockStatusDesc: String = getString(component.localization.Strings.StockStatusDesc)
    val trackQuantityDesc: String = getString(component.localization.Strings.TrackQuantityDesc)
    val priceDesc: String = getString(component.localization.Strings.PriceDesc)
    val regularPriceDesc: String = getString(component.localization.Strings.RegularPriceDesc)
    val weightDesc: String = getString(component.localization.Strings.WeightDesc)
    val lengthDesc: String = getString(component.localization.Strings.LengthDesc)
    val widthDesc: String = getString(component.localization.Strings.WidthDesc)
    val heightDesc: String = getString(component.localization.Strings.HeightDesc)
    val shippingPresetDesc: String = getString(component.localization.Strings.ShippingPresetDesc)
    val deleteExplain: String = getString(component.localization.Strings.DeleteExplain)
    val chargeTax: String = getString(component.localization.Strings.ChargeTax)
    val chargeTaxDesc: String = getString(component.localization.Strings.ChargeTaxDesc)
    val useGlobalTracking: String = getString(component.localization.Strings.UseGlobalTracking)
    val useGlobalTrackingDesc: String = getString(component.localization.Strings.UseGlobalTrackingDesc)
    val media: String = getString(component.localization.Strings.Media)
    val info: String = getString(component.localization.Strings.Info)
    val insights: String = getString(component.localization.Strings.Insights)
    val noInsights: String = getString(component.localization.Strings.NoInsights)
    val productOrganization: String = getString(component.localization.Strings.ProductOrganization)
    val variants: String = getString(component.localization.Strings.Variants)
    val addOptionsLikeSizeOrColor: String = getString(component.localization.Strings.AddOptionsLikeSizeOrColor)
    val addAnotherOption: String = getString(component.localization.Strings.AddAnotherOption)
}
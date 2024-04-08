package feature.admin.product.edit

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

        // Variants
        val showAddOptions: Boolean = false, // When no variants
        val showAddAnotherOption: Boolean = false, // When has variants, but less than 3
        val localOptions: List<LocalOption> = listOf(),
        val localVariants: List<LocalVariant> = listOf(),
        val totalInventory: Int = 0,
        val variantEditingEnabled: Boolean = false,
    )

    data class LocalOption(
        val name: String = "",
        val attrs: List<Attribute> = listOf(Attribute()),
        val isEditing: Boolean = true,
        val nameError: String? = null,
        val isDoneDisabled: Boolean = true,
    )

    data class Attribute(
        val value: String = "",
        val error: String? = null,
    )

    data class LocalVariant(
        val attrs: List<String> = emptyList(),
        val price: String = "500.00",
        val quantity: String = "2",
        val enabled: Boolean = true,
        val priceError: String? = null,
        val quantityError: String? = null,
    )

    sealed interface Inputs {
        data object OnAddOptionsClick : Inputs
        data object OnAddAnotherOptionClick : Inputs
        data class SetShowAddOptions(val show: Boolean) : Inputs
        data class SetShowAddAnotherOption(val show: Boolean) : Inputs
        data class SetLocalOptions(val localOptions: List<LocalOption>) : Inputs
        data class OnOptionNameChanged(val optionIndex: Int, val name: String) : Inputs
        data class OnOptionAttrValueChanged(val optionIndex: Int, val attrIndex: Int, val value: String) : Inputs
        data class OnOptionDoneClicked(val optionIndex: Int) : Inputs
        data class OnEditOptionClicked(val optionIndex: Int) : Inputs
        data class OnDeleteOptionClicked(val optionIndex: Int) : Inputs
        data class OnDeleteOptionAttrClicked(val optionIndex: Int, val attrIndex: Int) : Inputs
        data class OnUndoDeleteVariantClicked(val deletedVariantIndex: Int) : Inputs

        data class OnVariantPriceChanged(val variantIndex: Int, val price: String) : Inputs
        data class OnVariantQuantityChanged(val variantIndex: Int, val quantity: String) : Inputs
        data class OnDeleteVariantClicked(val variantIndex: Int) : Inputs

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

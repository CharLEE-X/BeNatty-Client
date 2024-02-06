package feature.admin.product.page

import component.localization.getString
import data.GetCategoriesAllMinimalQuery
import data.ProductGetByIdQuery
import data.service.AuthService
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.StockStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object AdminProductPageContract : KoinComponent {
    private val authService by inject<AuthService>()

    data class State(
        val isLoading: Boolean = false,
        val screenState: ScreenState,

        // Common
        val id: String? = null,
        val name: String = "",
        val nameError: String? = null,
        val shakeName: Boolean = false,
        val isCreateProductButtonDisabled: Boolean = true,
        val shortDescription: String = "",
        val shortDescriptionError: String? = null,
        val shakeShortDescription: Boolean = false,
        val isFeatured: Boolean = false,
        val allowReviews: Boolean = true,
        val catalogVisibility: CatalogVisibility = CatalogVisibility.Everywhere,
        val categories: List<String> = emptyList(),
        val tags: List<String> = emptyList(),
        val relatedIds: List<String> = emptyList(),
        val isCommonDetailsEditing: Boolean = false,
        val isSaveCommonDetailsButtonDisabled: Boolean = true,
        val creator: ProductGetByIdQuery.Creator = ProductGetByIdQuery.Creator(
            id = authService.userId.toString(),
            name = "",
        ),
        val createdAt: String = "",
        val updatedAt: String = "",
        val postStatus: PostStatus = PostStatus.Draft,

        val description: String? = null,
        val descriptionError: String? = null,
        val shakeDescription: Boolean = false,

        val isDataEditing: Boolean = false,
        val isSaveDataButtonDisabled: Boolean = true,
        val isPurchasable: Boolean = true,

        val allCategories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal> = emptyList(),

        val original: ProductGetByIdQuery.GetProductById = ProductGetByIdQuery.GetProductById(
            product = ProductGetByIdQuery.Product(
                id = id ?: "",
                common = ProductGetByIdQuery.Common(
                    name = name,
                    shortDescription = shortDescription,
                    allowReviews = allowReviews,
                    catalogVisibility = catalogVisibility,
                    categories = categories,
                    isFeatured = isFeatured,
                    relatedIds = relatedIds,
                    tags = tags,
                    createdBy = creator,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                ),
                data = ProductGetByIdQuery.Data1(
                    postStatus = postStatus,
                    description = description,
                    isPurchasable = isPurchasable,
                    images = listOf(),
                    parentId = null,
                ),
                inventory = ProductGetByIdQuery.Inventory(
                    backorderStatus = BackorderStatus.Allowed,
                    canBackorder = true,
                    isOnBackorder = true,
                    lowStockThreshold = 0,
                    onePerOrder = true,
                    remainingStock = 0,
                    stockStatus = StockStatus.InStock,
                    trackInventory = true,
                ),
                price = ProductGetByIdQuery.Price(
                    price = "0.00",
                    regularPrice = "0.00",
                    salePrice = "0.00",
                    onSale = true,
                    saleStart = null,
                    saleEnd = null,
                ),
                shipping = ProductGetByIdQuery.Shipping(
                    height = "",
                    length = "",
                    weight = "",
                    width = "",
                    requiresShipping = true,
                ),
            ),
            creator = creator,
            reviews = emptyList(),
            totalInWishlist = 0,
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val productId: String?) : Inputs

        sealed interface Get : Inputs {
            data class ProductById(val id: String) : Inputs
            data object AllCategories : Inputs
        }

        sealed interface OnClick : Inputs {
            data object Create : OnClick
            data object SaveCommonDetails : Inputs
            data object SaveDataDetails : Inputs
            data object EditDetails : OnClick
            data object EditData : OnClick
            data object CancelEditDetails : OnClick
            data object CancelEditData : OnClick
            data object Delete : OnClick
            data class Category(val category: String) : Inputs
            data object Creator : Inputs
        }

        sealed interface Set : Inputs {
            data class AllCategories(val categories: List<GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal>) :
                Inputs

            data class Loading(val isLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs
            data class OriginalProduct(val product: ProductGetByIdQuery.GetProductById) : Inputs

            data class Id(val id: String) : Inputs
            data class Name(val name: String) : Inputs
            data class NameShake(val shake: Boolean) : Inputs
            data class ShortDescription(val shortDescription: String) : Inputs
            data class ShortDescriptionShake(val shake: Boolean) : Inputs
            data class IsFeatured(val isFeatured: Boolean) : Inputs
            data class AllowReviews(val allowReviews: Boolean) : Inputs
            data class VisibilityInCatalog(val catalogVisibility: CatalogVisibility) : Inputs
            data class IsCommonDetailsEditable(val isEditable: Boolean) : Inputs
            data class IsCommonDetailsButtonDisabled(val isDisabled: Boolean) : Inputs
            data class Creator(val creator: ProductGetByIdQuery.Creator) : Inputs
            data class CreatedAt(val createdAt: String) : Inputs
            data class UpdatedAt(val updatedAt: String) : Inputs
            data class Categories(val categories: List<String>) : Inputs
            data class StatusOfPost(val postStatus: PostStatus) : Inputs
            data class Description(val description: String) : Inputs
            data class DescriptionShake(val shake: Boolean) : Inputs
            data class IsPurchasable(val isPurchasable: Boolean) : Inputs
            data class IsDataEditable(val isEditable: Boolean) : Inputs
            data class IsDataButtonDisabled(val isDisabled: Boolean) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToProductList : Events
        data class GoToUserDetails(val userId: String) : Events
        data object GoToCreateCategory : Events
        data object GoToCreateTag : Events
    }

    data class Strings(
        val products: String = getString(component.localization.Strings.Products),
        val country: String = getString(component.localization.Strings.Country),
        val edit: String = getString(component.localization.Strings.Edit),
        val save: String = getString(component.localization.Strings.Save),
        val cancel: String = getString(component.localization.Strings.Cancel),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val updatedAt: String = getString(component.localization.Strings.UpdatedAt),
        val never: String = getString(component.localization.Strings.Never),
        val details: String = getString(component.localization.Strings.Details),
        val name: String = getString(component.localization.Strings.Name),
        val create: String = getString(component.localization.Strings.Create),
        val shortDescription: String = getString(component.localization.Strings.ShortDescription),
        val isFeatured: String = getString(component.localization.Strings.IsFeatured),
        val allowReviews: String = getString(component.localization.Strings.AllowReviews),
        val catalogVisibility: String = getString(component.localization.Strings.CatalogVisibility),
        val createProduct: String = getString(component.localization.Strings.CreateProduct),
        val product: String = getString(component.localization.Strings.Product),
        val categories: String = getString(component.localization.Strings.Categories),
        val tags: String = getString(component.localization.Strings.Tags),
        val postStatus: String = getString(component.localization.Strings.PostStatus),
        val data: String = getString(component.localization.Strings.Data),
        val description: String = getString(component.localization.Strings.Description),
        val isPurchasable: String = getString(component.localization.Strings.IsPurchasable),
    )

    sealed interface ScreenState {
        data object New : ScreenState

        sealed interface Existing : ScreenState {
            data object Read : Existing
            data object Edit : Existing
        }
    }
}

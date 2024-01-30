package feature.admin.product.page

import com.apollographql.apollo3.mpp.currentTimeMillis
import component.localization.getString
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

        val screenState: ScreenState = ScreenState.New,

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

        val createdAt: Long = currentTimeMillis(),
        val createdBy: String = authService.userId.toString(),
        val updatedAt: Long = currentTimeMillis(),

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
                    createdBy = createdBy,
                    createdAt = createdAt.toString(),
                    updatedAt = updatedAt.toString(),
                    relatedIds = relatedIds,
                    tags = tags,
                ),
                data = ProductGetByIdQuery.Data1(
                    description = "",
                    images = listOf(),
                    isPurchasable = true,
                    parentId = null,
                    postStatus = PostStatus.Draft,
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
            reviews = emptyList(),
            totalInWishlist = 0,
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val productId: String?) : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetScreenState(val screenState: ScreenState) : Inputs

        data object CreateNewProduct : Inputs
        data class GetProductById(val id: String) : Inputs
        data object DeleteProduct : Inputs

        data class SetId(val id: String) : Inputs
        data class SetProduct(val product: ProductGetByIdQuery.GetProductById) : Inputs

        data class SetName(val name: String) : Inputs
        data class SetNameShake(val shake: Boolean) : Inputs

        data class SetShortDescription(val shortDescription: String) : Inputs
        data class SetShortDescriptionShake(val shake: Boolean) : Inputs

        data class SetCommonDetailsEditable(val isEditable: Boolean) : Inputs
        data class SetCommonDetailsButtonDisabled(val isDisabled: Boolean) : Inputs
        data object SaveCommonDetails : Inputs

        data class SetCreatedBy(val createdBy: String) : Inputs
        data class SetCreatedAt(val createdAt: Long) : Inputs
        data class SetUpdatedAt(val updatedAt: Long) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToProductList : Events
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
    )

    sealed interface ScreenState {
        data object New : ScreenState

        sealed interface Existing : ScreenState {
            data object Read : Existing
            data object Edit : Existing
        }
    }
}

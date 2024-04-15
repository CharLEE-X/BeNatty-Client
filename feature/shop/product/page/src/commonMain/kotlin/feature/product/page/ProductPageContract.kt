package feature.product.page

import data.AdminProductGetByIdQuery
import data.type.BackorderStatus
import data.type.PostStatus
import data.type.StockStatus

object ProductPageContract {
    data class State(
        val isLoading: Boolean = true,

        val product: AdminProductGetByIdQuery.GetProductById = AdminProductGetByIdQuery.GetProductById(
            id = "",
            name = "",
            description = "",
            tags = emptyList(),
            variants = emptyList(),
            createdAt = "",
            updatedAt = "",
            postStatus = PostStatus.Draft,
            sold = 0,
            media = listOf(),
            categoryId = null,
            isFeatured = false,
            allowReviews = false,
            creator = AdminProductGetByIdQuery.Creator(id = "", name = ""),
            pricing = AdminProductGetByIdQuery.Pricing(price = null, regularPrice = null, chargeTax = false),
            inventory = AdminProductGetByIdQuery.Inventory(
                trackQuantity = false,
                useGlobalTracking = false,
                remainingStock = 0,
                stockStatus = StockStatus.InStock,
                lowStockThreshold = 0,
                backorderStatus = BackorderStatus.Allowed
            ),
            shipping = AdminProductGetByIdQuery.Shipping(
                presetId = null,
                isPhysicalProduct = false,
                weight = null,
                length = null,
                width = null,
                height = null
            ),
            reviews = listOf(),
            totalInWishlist = 0,
        ),
    )

    sealed interface Inputs {
        data class Init(val productId: String) : Inputs
        data class FetchProduct(val productId: String) : Inputs

        data class SetLoading(val loading: Boolean) : Inputs
        data class SetProduct(val product: AdminProductGetByIdQuery.GetProductById) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }
}

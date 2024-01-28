package feature.admin.product.page

import component.localization.getString
import data.ProductsGetAllPageQuery

object AdminProductPageContract {
    data class State(
        val products: List<ProductsGetAllPageQuery.Product> = emptyList(),
        val info: ProductsGetAllPageQuery.Info = ProductsGetAllPageQuery.Info(
            count = 0,
            pages = 0,
            prev = null,
            next = null,
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class GetProductsPage(val page: Int) : Inputs
        data class SetProductsPage(
            val products: List<ProductsGetAllPageQuery.Product>,
            val info: ProductsGetAllPageQuery.Info
        ) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val products: String = getString(component.localization.Strings.Products),
    )
}

package feature.admin.orders

import component.localization.getString
import data.GetProductsPageQuery

object AdminProductsContract {
    data class State(
        val products: List<GetProductsPageQuery.Product> = emptyList(),
        val info: GetProductsPageQuery.Info = GetProductsPageQuery.Info(
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
            val products: List<GetProductsPageQuery.Product>,
            val info: GetProductsPageQuery.Info
        ) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val products: String = getString(component.localization.Strings.Products),
    )
}

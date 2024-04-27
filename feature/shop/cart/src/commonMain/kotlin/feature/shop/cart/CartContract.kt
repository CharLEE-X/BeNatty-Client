package feature.shop.cart

import core.models.Currency
import data.GetTopSellingProductsQuery
import data.GetUserCartQuery

object CartContract {
    data class State(
        val cartLoading: Boolean = true,
        val topProductsLoading: Boolean = true,
        val showSidebar: Boolean = false,

        val items: List<GetUserCartQuery.Item> = emptyList(),
        val topSellingProducts: List<GetTopSellingProductsQuery.GetTopSellingProduct> = emptyList(),

        val currency: Currency = Currency("£", "GBP"),
        val showSpendMore: Boolean = false,
        val spendMoreKey: String = "",
        val spendMoreValue: String = "",
        val basketCount: Int = 0,
        val subtotal: String = "0",
        val saved: String? = null,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchCart : Inputs
        data object FetchTopProducts : Inputs
        data class UpdateCart(val productId: String, val variantId: String, val quantity: Int = 1) : Inputs

        data object OnLoginClicked : Inputs
        data object ShowCart : Inputs
        data object HideCart : Inputs
        data class OnAddToCartClicked(val productId: String, val variantId: String) : Inputs
        data class OnTopProductClicked(val productId: String) : Inputs
        data class OnDecrementClicked(val productId: String, val variantId: String) : Inputs
        data class OnIncrementClicked(val productId: String, val variantId: String) : Inputs
        data class OnRemoveClicked(val productId: String, val variantId: String) : Inputs
        data object OnGoToCheckoutClicked : Inputs

        data class SetCartLoading(val loading: Boolean) : Inputs
        data class SetShowSidebar(val show: Boolean) : Inputs
        data class SetTopProducts(val products: List<GetTopSellingProductsQuery.GetTopSellingProduct>) : Inputs
        data class SetTopProductsLoading(val loading: Boolean) : Inputs
        data class SetItems(val items: List<GetUserCartQuery.Item>) : Inputs
        data class SetCurrency(val currency: Currency) : Inputs
        data class SetSpendMore(val show: Boolean, val key: String, val value: String) : Inputs
        data class SetBasketCount(val count: Int) : Inputs
        data class SetTotals(val subtotal: String, val saved: String?) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToLogin : Events
        data class GoToProduct(val productId: String) : Events
        data object GoToCheckout : Events
    }
}

package feature.checkout

import core.models.Currency
import data.GetUserCartQuery
import data.type.PaymentType

object CheckoutContract {
    data class State(
        val isLoading: Boolean = true,
        val items: List<CartItem> = emptyList(),
        val subtotal: Double = 0.0,
        val totalPrice: String = "",
        val currency: Currency = Currency("£", "GBP"),
        val shippingPrice: Double? = null,
        val shippingMessage: String = "",
        val paymentTypes: List<PaymentType> = emptyList(),
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchCart : Inputs
        data object FetchPaymentMethods : Inputs

        data class OnPaymentMethodClicked(val method: PaymentType) : Inputs

        data class SetCart(val items: List<CartItem>, val subtotal: Double, val currency: Currency) : Inputs
        data class SetShippingPrice(val price: Double?) : Inputs
        data class SetTotalPrice(val totalPrice: String) : Inputs
        data class SetCurrency(val currency: Currency) : Inputs
        data class SetPaymentMethods(val methods: List<PaymentType>) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class CartItem(
        val mediaUrl: String?,
        val quantity: Int,
        val name: String,
        val attrs: List<GetUserCartQuery.Attr>,
        val price: Double,
    )

    data class PaymentMethod(
        val type: PaymentType,
        val name: String,
        val iconUrl: String,
    )
}

fun PaymentType.toPaymentMethod(): CheckoutContract.PaymentMethod = when (this) {
    PaymentType.VISA -> CheckoutContract.PaymentMethod(
        type = this,
        name = "Visa",
        iconUrl = "https://cdn-icons-png.flaticon.com/128/196/196578.png",
    )

    PaymentType.PAYPAL -> CheckoutContract.PaymentMethod(
        type = this,
        name = "PayPal",
        iconUrl = "https://cdn-icons-png.flaticon.com/128/196/196565.png",
    )

    PaymentType.GOOGLE_PAY -> CheckoutContract.PaymentMethod(
        type = this,
        name = "Google Pay",
        iconUrl = "https://google.com",
    )

    PaymentType.APPLE_PAY -> CheckoutContract.PaymentMethod(
        type = this,
        name = "Apple Pay",
        iconUrl = "https://apple.com",
    )

    PaymentType.UNKNOWN__ -> CheckoutContract.PaymentMethod(
        type = this,
        name = "Unknown",
        iconUrl = "https://unknown.com",
    )
}

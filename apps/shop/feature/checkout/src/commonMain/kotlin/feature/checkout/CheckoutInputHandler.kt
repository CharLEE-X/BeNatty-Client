package feature.checkout

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.service.PaymentService
import data.service.UserService
import data.type.PaymentType
import data.type.Platform
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.pow
import kotlin.math.roundToInt

private typealias InputScope =
    InputHandlerScope<CheckoutContract.Inputs, CheckoutContract.Events, CheckoutContract.State>

internal class CheckoutInputHandler :
    KoinComponent,
    InputHandler<CheckoutContract.Inputs, CheckoutContract.Events, CheckoutContract.State> {

    private val userService: UserService by inject()
    private val paymentService: PaymentService by inject()

    override suspend fun InputScope.handleInput(input: CheckoutContract.Inputs) = when (input) {
        is CheckoutContract.Inputs.Init -> handleInit()
        CheckoutContract.Inputs.FetchCart -> handleFetchCart()
        CheckoutContract.Inputs.FetchPaymentMethods -> handleFetchPaymentMethods()

        is CheckoutContract.Inputs.OnPaymentMethodClicked -> handlePaymentMethodClicked(input.method)

        is CheckoutContract.Inputs.SetShippingPrice -> updateState {
            it.copy(
                shippingPrice = input.price,
                shippingMessage = when {
                    input.price == null -> getString(Strings.EnterShippingAddress)
                    input.price == 0.0 -> getString(Strings.FreeShipping)
                    input.price > 0.0 -> input.price.toString()
                    else -> getString(Strings.EnterShippingAddress)
                },
            )
        }

        is CheckoutContract.Inputs.SetCart -> updateState {
            it.copy(
                items = input.items,
                subtotal = input.subtotal,
                currency = input.currency,
            )
        }

        is CheckoutContract.Inputs.SetCurrency -> updateState { it.copy(currency = input.currency) }
        is CheckoutContract.Inputs.SetTotalPrice -> updateState { it.copy(totalPrice = input.totalPrice) }
        is CheckoutContract.Inputs.SetPaymentMethods -> updateState { it.copy(paymentTypes = input.methods) }
    }

    private suspend fun InputScope.handlePaymentMethodClicked(method: PaymentType) {
        noOp()
    }

    private suspend fun InputScope.handleFetchPaymentMethods() {
        sideJob("FetchPaymentMethods") {
            val platforms = listOf(Platform.APPLE)
            paymentService.getPaymentMethods(platforms).fold(
                { postEvent(CheckoutContract.Events.OnError(it.toString())) },
                { postInput(CheckoutContract.Inputs.SetPaymentMethods(it.getPaymentMethods)) },
            )
        }
    }

    private suspend fun InputScope.handleFetchCart() {
        val state = getCurrentState()
        sideJob("FetchCart") {
            userService.getCart().fold(
                { postEvent(CheckoutContract.Events.OnError(it.toString())) },
            ) {
                // TODO: This may need to go to the Config
                val currency = Currency("£", "GBP")
                postInput(CheckoutContract.Inputs.SetCurrency(currency))

                postInput(
                    CheckoutContract.Inputs.SetCart(
                        it.getUserCart.items.map {
                            CheckoutContract.CartItem(
                                mediaUrl = it.mediaUrl,
                                quantity = it.quantity,
                                name = it.name,
                                attrs = it.attrs,
                                price = (it.salePrice ?: it.regularPrice) * it.quantity,
                            )
                        },
                        subtotal = it.getUserCart.subtotal,
                        currency = currency,
                    ),
                )
                postInput(CheckoutContract.Inputs.SetShippingPrice(null))

                val total = it.getUserCart.subtotal + (state.shippingPrice ?: 0.0)
                val totalString = total.toFloat().toString(2)
                postInput(CheckoutContract.Inputs.SetTotalPrice(totalString))
            }
        }
    }

    fun Float.toString(numOfDec: Int): String {
        val integerDigits = this.toInt()
        val floatDigits = ((this - integerDigits) * 10f.pow(numOfDec)).roundToInt()
        val floatDigitsString = floatDigits.toString().padStart(numOfDec, '0')
        return "$integerDigits.$floatDigitsString"
    }

    private suspend fun InputScope.handleInit() {
        sideJob("InitCheckout") {
            postInput(CheckoutContract.Inputs.FetchCart)
            postInput(CheckoutContract.Inputs.FetchPaymentMethods)
            postInput(CheckoutContract.Inputs.SetShippingPrice(null)) // FIXME: This should be fetched from the API
        }
    }
}

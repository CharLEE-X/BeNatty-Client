package feature.shop.cart

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.GetUserCartQuery
import data.service.ProductService
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<CartContract.Inputs, CartContract.Events, CartContract.State>

internal class CartInputHandler :
    KoinComponent,
    InputHandler<CartContract.Inputs, CartContract.Events, CartContract.State> {

    private val productService: ProductService by inject()
    private val userService: UserService by inject()

    override suspend fun InputHandlerScope<
        CartContract.Inputs,
        CartContract.Events,
        CartContract.State,
        >.handleInput(
        input: CartContract.Inputs,
    ) = when (input) {
        CartContract.Inputs.Init -> handleInit()
        CartContract.Inputs.FetchCart -> handleFetchCart()
        CartContract.Inputs.FetchTopProducts -> handleFetchTopProducts()

        CartContract.Inputs.ShowCart -> handleShowSidebar()
        CartContract.Inputs.HideCart -> handleHideSidebar()
        CartContract.Inputs.OnLoginClicked -> postEvent(CartContract.Events.GoToLogin)

        is CartContract.Inputs.SetCartLoading -> updateState { it.copy(cartLoading = input.loading) }
        is CartContract.Inputs.SetTopProductsLoading -> updateState { it.copy(topProductsLoading = input.loading) }
        is CartContract.Inputs.SetShowSidebar -> updateState { it.copy(showSidebar = input.show) }
        is CartContract.Inputs.SetTopProducts -> updateState { it.copy(topSellingProducts = input.products) }
        is CartContract.Inputs.OnTopProductClicked -> postEvent(CartContract.Events.GoToProduct(input.productId))
        is CartContract.Inputs.OnAddToCartClicked -> handleOnAddToCartClicked(
            productId = input.productId,
            variantId = input.variantId,
        )

        is CartContract.Inputs.UpdateCart -> handleUpdateCart(
            productId = input.productId,
            variantId = input.variantId,
            quantity = input.quantity,
        )

        is CartContract.Inputs.SetCart -> updateState { it.copy(cart = input.cart) }
        is CartContract.Inputs.SetCurrency -> updateState { it.copy(currency = input.currency) }
        is CartContract.Inputs.SetSpendMore ->
            updateState { it.copy(showSpendMore = input.show, spendMoreKey = input.key, spendMoreValue = input.value) }

        is CartContract.Inputs.OnDecrementClicked -> handleDecrementClicked(input.productId, input.variantId)
        is CartContract.Inputs.OnIncrementClicked -> handleIncrementClicked(input.productId, input.variantId)
        is CartContract.Inputs.OnRemoveClicked -> handleRemoveClicked(input.productId, input.variantId)
        CartContract.Inputs.OnGoToCheckoutClicked -> handleOnGoToCheckoutClicked()
        is CartContract.Inputs.SetBasketCount -> updateState { it.copy(basketCount = input.count) }
        is CartContract.Inputs.SetTotals -> updateState { it.copy(subtotal = input.subtotal, saved = input.saved) }
    }

    private suspend fun InputScope.handleOnGoToCheckoutClicked() {
        sideJob("handleOnGoToCheckoutClicked") {
            postInput(CartContract.Inputs.HideCart)
            postEvent(CartContract.Events.GoToCheckout)
        }
    }

    private suspend fun InputScope.handleOnAddToCartClicked(productId: String, variantId: String) {
        getCurrentState().cart.items
            .firstOrNull { it.productId == productId && it.variantId == variantId }
            ?.let { postInput(CartContract.Inputs.UpdateCart(productId, variantId, it.quantity + 1)) }
            ?: handleUpdateCart(productId, variantId, 1)
    }

    private suspend fun InputScope.handleRemoveClicked(productId: String, variantId: String) {
        sideJob("removeFromCart") {
            userService.removeItemFromCart(
                productId = productId,
                variantId = variantId,
            ).fold(
                { postEvent(CartContract.Events.OnError(it.toString())) },
                {
                    postInput(
                        CartContract.Inputs.SetCart(
                            GetUserCartQuery.GetUserCart(
                                guestCartId = it.removeItemFromUserCart.guestCartId,
                                items = it.removeItemFromUserCart.items.map {
                                    GetUserCartQuery.Item(
                                        productId = it.productId,
                                        variantId = it.variantId,
                                        vendor = it.vendor,
                                        name = it.name,
                                        attrs = it.attrs.map {
                                            GetUserCartQuery.Attr(
                                                key = it.key,
                                                value = it.value,
                                            )
                                        },
                                        mediaUrl = it.mediaUrl,
                                        regularPrice = it.regularPrice,
                                        salePrice = it.salePrice,
                                        quantity = it.quantity,
                                        available = it.available,
                                    )
                                },
                                subtotal = it.removeItemFromUserCart.subtotal,
                                saved = it.removeItemFromUserCart.saved,
                            ),
                        ),
                    )

                    val totals = countTotals(
                        it.removeItemFromUserCart.items.map {
                            Prices(
                                regularPrice = it.regularPrice,
                                salePrice = it.salePrice,
                                quantity = it.quantity,
                            )
                        },
                    )
                    postInput(CartContract.Inputs.SetTotals(subtotal = totals.subtotal, saved = totals.saved))
                    postInput(CartContract.Inputs.SetBasketCount(it.removeItemFromUserCart.items.size))
                },
            )
        }
    }

    private suspend fun InputScope.handleDecrementClicked(productId: String, variantId: String) {
        getCurrentState().cart.items
            .firstOrNull { it.productId == productId && it.variantId == variantId }
            ?.let {
                if (it.quantity == 1) {
                    postInput(CartContract.Inputs.OnRemoveClicked(productId, variantId))
                } else {
                    postInput(CartContract.Inputs.UpdateCart(productId, variantId, it.quantity - 1))
                }
            } ?: noOp()
    }

    private suspend fun InputScope.handleIncrementClicked(productId: String, variantId: String) {
        getCurrentState().cart.items
            .firstOrNull { it.productId == productId && it.variantId == variantId }
            ?.let { postInput(CartContract.Inputs.UpdateCart(productId, variantId, it.quantity + 1)) }
            ?: noOp()
    }

    private suspend fun InputScope.handleUpdateCart(productId: String, variantId: String, quantity: Int) {
        sideJob("addToCart") {
            userService.addProductToCart(
                productId = productId,
                variantId = variantId,
                quantity = quantity,
            ).fold(
                { postEvent(CartContract.Events.OnError(it.toString())) },
                {
                    postInput(
                        CartContract.Inputs.SetCart(
                            GetUserCartQuery.GetUserCart(
                                guestCartId = it.addToCart.guestCartId,
                                items = it.addToCart.items.map {
                                    GetUserCartQuery.Item(
                                        productId = it.productId,
                                        variantId = it.variantId,
                                        vendor = it.vendor,
                                        name = it.name,
                                        attrs = it.attrs.map {
                                            GetUserCartQuery.Attr(
                                                key = it.key,
                                                value = it.value,
                                            )
                                        },
                                        mediaUrl = it.mediaUrl,
                                        regularPrice = it.regularPrice,
                                        salePrice = it.salePrice,
                                        quantity = it.quantity,
                                        available = it.available,
                                    )
                                },
                                subtotal = it.addToCart.subtotal,
                                saved = it.addToCart.saved,
                            ),
                        ),
                    )

                    val totals = countTotals(
                        it.addToCart.items.map {
                            Prices(
                                regularPrice = it.regularPrice,
                                salePrice = it.salePrice,
                                quantity = it.quantity,
                            )
                        },
                    )
                    postInput(CartContract.Inputs.SetTotals(subtotal = totals.subtotal, saved = totals.saved))
                    postInput(CartContract.Inputs.SetShowSidebar(true))
                    postInput(CartContract.Inputs.SetBasketCount(it.addToCart.items.size))
                },
            )
        }
    }

    private suspend fun InputScope.handleFetchCart() {
        val state = getCurrentState()
        sideJob("fetchCart") {
            userService.getCart().fold(
                { postEvent(CartContract.Events.OnError(it.toString())) },
                {
                    // TODO: This may need to go to the Config
                    val currency = Currency("£", "GBP")
                    postInput(CartContract.Inputs.SetCurrency(currency))

                    // TODO: Add spend more stages
                    val showSpendMore = true
                    val spendMoreKey = getString(Strings.FreeShipping)
                    val spendMoreValue = "100.00"
                    postInput(CartContract.Inputs.SetSpendMore(showSpendMore, spendMoreKey, spendMoreValue))

                    val totals = countTotals(
                        it.getUserCart.items.map {
                            Prices(
                                regularPrice = it.regularPrice,
                                salePrice = it.salePrice,
                                quantity = it.quantity,
                            )
                        },
                    )
                    postInput(CartContract.Inputs.SetTotals(subtotal = totals.subtotal, saved = totals.saved))
                    postInput(CartContract.Inputs.SetCart(it.getUserCart))
                    postInput(CartContract.Inputs.SetBasketCount(it.getUserCart.items.size))
                },
            )
            if (state.cartLoading) postInput(CartContract.Inputs.SetCartLoading(false))
        }
    }

    data class Prices(val regularPrice: Double, val salePrice: Double?, val quantity: Int)
    data class Totals(val subtotal: String, val saved: String)

    private fun countTotals(prices: List<Prices>): Totals {
        val subtotal = prices.sumOf { (it.salePrice ?: it.regularPrice) * it.quantity }
        val regulars = prices.sumOf { it.regularPrice * it.quantity }
        val savedTotal = subtotal - regulars
        val saved = if (savedTotal < 0) -savedTotal else savedTotal
        return Totals(subtotal.toString(), saved.toString())
    }

    private suspend fun InputScope.handleFetchTopProducts() {
        sideJob("fetchTopProducts") {
            postInput(CartContract.Inputs.SetTopProductsLoading(true))
            productService.getTopSellingProducts().fold(
                { postEvent(CartContract.Events.OnError(it.toString())) },
                { postInput(CartContract.Inputs.SetTopProducts(it.getTopSellingProducts)) },
            )
            postInput(CartContract.Inputs.SetTopProductsLoading(false))
        }
    }

    private suspend fun InputScope.handleShowSidebar() {
        postInput(CartContract.Inputs.SetShowSidebar(true))
    }

    private suspend fun InputScope.handleHideSidebar() {
        postInput(CartContract.Inputs.SetShowSidebar(false))
    }

    private suspend fun InputScope.handleInit() {
        postInput(CartContract.Inputs.SetCartLoading(true))
        postInput(CartContract.Inputs.SetTopProductsLoading(true))
        postInput(CartContract.Inputs.FetchCart)
        postInput(CartContract.Inputs.FetchTopProducts)
    }
}

package feature.shop.cart

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias FooterEventHandlerScope =
    EventHandlerScope<CartContract.Inputs, CartContract.Events, CartContract.State>

internal class CartEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToLogin: () -> Unit,
    private val goToProduct: (String) -> Unit,
    private val goToCheckout: () -> Unit,
) : KoinComponent, EventHandler<CartContract.Inputs, CartContract.Events, CartContract.State> {
    override suspend fun FooterEventHandlerScope.handleEvent(event: CartContract.Events) = when (event) {
        is CartContract.Events.OnError -> onError(event.message)
        CartContract.Events.GoToLogin -> goToLogin()
        is CartContract.Events.GoToProduct -> goToProduct(event.productId)
        CartContract.Events.GoToCheckout -> goToCheckout()
    }
}

package feature.shop.account.orders

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class OrdersEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<OrdersContract.Inputs, OrdersContract.Events, OrdersContract.State> {
    override suspend fun EventHandlerScope<OrdersContract.Inputs, OrdersContract.Events, OrdersContract.State>.handleEvent(
        event: OrdersContract.Events,
    ) = when (event) {
        is OrdersContract.Events.OnError -> onError(event.message)
    }
}

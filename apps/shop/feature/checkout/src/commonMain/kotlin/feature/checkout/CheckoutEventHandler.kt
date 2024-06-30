package feature.checkout

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class CheckoutEventHandler(
    private val onError: suspend (String) -> Unit,
) :
    EventHandler<CheckoutContract.Inputs, CheckoutContract.Events, CheckoutContract.State> {
    override suspend fun EventHandlerScope<
        CheckoutContract.Inputs,
        CheckoutContract.Events,
        CheckoutContract.State,
        >.handleEvent(
        event: CheckoutContract.Events,
    ) = when (event) {
        is CheckoutContract.Events.OnError -> onError(event.message)
    }
}

package feature.account.wishlist

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class WishlistEventHandler(
    private val onError: suspend (String) -> Unit,
) : EventHandler<WishlistContract.Inputs, WishlistContract.Events, WishlistContract.State> {
    override suspend fun EventHandlerScope<WishlistContract.Inputs, WishlistContract.Events, WishlistContract.State>.handleEvent(
        event: WishlistContract.Events,
    ) = when (event) {
        is WishlistContract.Events.OnError -> onError(event.message)
    }
}

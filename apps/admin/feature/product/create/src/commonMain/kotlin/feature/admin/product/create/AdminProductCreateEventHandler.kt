package feature.admin.product.create

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductCreateEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: suspend () -> Unit,
    private val goToProduct: suspend (String) -> Unit,
) : EventHandler<
    AdminProductCreateContract.Inputs,
    AdminProductCreateContract.Events,
    AdminProductCreateContract.State> {
    override suspend fun EventHandlerScope<
        AdminProductCreateContract.Inputs,
        AdminProductCreateContract.Events,
        AdminProductCreateContract.State>.handleEvent(event: AdminProductCreateContract.Events) =
        when (event) {
            is AdminProductCreateContract.Events.OnError -> onError(event.message)
            AdminProductCreateContract.Events.GoBack -> goBack()
            is AdminProductCreateContract.Events.GoToProduct -> goToProduct(event.id)
        }
}

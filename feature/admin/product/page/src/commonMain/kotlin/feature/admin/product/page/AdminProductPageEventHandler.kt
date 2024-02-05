package feature.admin.product.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToProductList: suspend () -> Unit,
    private val goToCreateCategory: suspend () -> Unit,
    private val goToCreateTag: suspend () -> Unit,
    private val goToUserDetails: suspend (String) -> Unit,
) : EventHandler<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State> {
    override suspend fun EventHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>.handleEvent(
        event: AdminProductPageContract.Events,
    ) = when (event) {
        is AdminProductPageContract.Events.OnError -> onError(event.message)
        AdminProductPageContract.Events.GoToProductList -> goToProductList()
        AdminProductPageContract.Events.GoToCreateCategory -> goToCreateCategory()
        AdminProductPageContract.Events.GoToCreateTag -> goToCreateTag()
        is AdminProductPageContract.Events.GoToUserDetails -> goToUserDetails(event.userId)
    }
}

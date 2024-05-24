package feature.admin.product.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminProductEditEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: suspend () -> Unit,
    private val goToCategoryCreate: suspend () -> Unit,
    private val goToTagCreate: suspend () -> Unit,
    private val goToCustomer: suspend (String) -> Unit,
    private val goToProduct: suspend (String) -> Unit,
) : EventHandler<AdminProductEditContract.Inputs, AdminProductEditContract.Events, AdminProductEditContract.State> {
    override suspend fun EventHandlerScope<AdminProductEditContract.Inputs, AdminProductEditContract.Events, AdminProductEditContract.State>.handleEvent(
        event: AdminProductEditContract.Events,
    ) = when (event) {
        is AdminProductEditContract.Events.OnError -> onError(event.message)
        AdminProductEditContract.Events.GoBackToProducts -> goBack()
        AdminProductEditContract.Events.GoToCreateCategory -> goToCategoryCreate()
        AdminProductEditContract.Events.GoToCreateTag -> goToTagCreate()
        is AdminProductEditContract.Events.GoToUserDetails -> goToCustomer(event.userId)
        is AdminProductEditContract.Events.GoToProduct -> goToProduct(event.id)
    }
}

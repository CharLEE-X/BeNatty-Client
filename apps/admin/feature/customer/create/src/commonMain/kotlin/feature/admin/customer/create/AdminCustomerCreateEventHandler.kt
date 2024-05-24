package feature.admin.customer.create

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCustomerCreateEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: () -> Unit,
    private val goToUser: suspend (String) -> Unit,
) : EventHandler<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State> {
    override suspend fun EventHandlerScope<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State>.handleEvent(
        event: AdminCustomerCreateContract.Events,
    ) = when (event) {
        is AdminCustomerCreateContract.Events.OnError -> onError(event.message)
        is AdminCustomerCreateContract.Events.GoBack -> goBack()
        is AdminCustomerCreateContract.Events.GoToCustomer -> goToUser(event.id)
    }
}

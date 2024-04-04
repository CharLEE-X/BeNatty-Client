package feature.admin.customer.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCustomerPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: () -> Unit,
    private val showLeavePageWarningDialog: () -> Unit,
    private val goToUser: suspend (String) -> Unit,
) : EventHandler<AdminCustomerPageContract.Inputs, AdminCustomerPageContract.Events, AdminCustomerPageContract.State> {
    override suspend fun EventHandlerScope<AdminCustomerPageContract.Inputs, AdminCustomerPageContract.Events, AdminCustomerPageContract.State>.handleEvent(
        event: AdminCustomerPageContract.Events,
    ) = when (event) {
        is AdminCustomerPageContract.Events.OnError -> onError(event.message)
        is AdminCustomerPageContract.Events.GoBack -> goBack()
        is AdminCustomerPageContract.Events.GoToUser -> goToUser(event.id)
        AdminCustomerPageContract.Events.ShowLeavePageWarningDialog -> showLeavePageWarningDialog()
    }
}

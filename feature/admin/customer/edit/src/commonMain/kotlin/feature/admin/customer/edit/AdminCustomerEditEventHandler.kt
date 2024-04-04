package feature.admin.customer.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCustomerEditEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goBack: () -> Unit,
    private val showLeavePageWarningDialog: () -> Unit,
    private val goToUser: suspend (String) -> Unit,
) : EventHandler<AdminCustomerEditContract.Inputs, AdminCustomerEditContract.Events, AdminCustomerEditContract.State> {
    override suspend fun EventHandlerScope<AdminCustomerEditContract.Inputs, AdminCustomerEditContract.Events, AdminCustomerEditContract.State>.handleEvent(
        event: AdminCustomerEditContract.Events,
    ) = when (event) {
        is AdminCustomerEditContract.Events.OnError -> onError(event.message)
        is AdminCustomerEditContract.Events.GoBack -> goBack()
        is AdminCustomerEditContract.Events.GoToCustomer -> goToUser(event.id)
        AdminCustomerEditContract.Events.ShowLeavePageWarningDialog -> showLeavePageWarningDialog()
    }
}

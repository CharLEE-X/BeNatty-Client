package feature.admin.customer.edit

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminCustomerEditViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    userId: String,
    goBack: () -> Unit,
    showLeavePageWarningDialog: () -> Unit,
    goToCustomerPage: (String) -> Unit,
) : BasicViewModel<
    AdminCustomerEditContract.Inputs,
    AdminCustomerEditContract.Events,
    AdminCustomerEditContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminCustomerEditContract.State(),
            inputHandler = AdminCustomerPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminCustomerEditEventHandler(
        onError = onError,
        goBack = goBack,
        showLeavePageWarningDialog = showLeavePageWarningDialog,
        goToUser = goToCustomerPage,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminCustomerEditContract.Inputs.Init(userId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

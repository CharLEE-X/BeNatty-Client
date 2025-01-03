package feature.admin.customer.create

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

class AdminCustomerCreateViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToCustomerPage: (String) -> Unit,
) : BasicViewModel<
    AdminCustomerCreateContract.Inputs,
    AdminCustomerCreateContract.Events,
    AdminCustomerCreateContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminCustomerCreateContract.State(),
            inputHandler = AdminCustomerCreateInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminCustomerCreateEventHandler(
        onError = onError,
        goBack = goBack,
        goToUser = goToCustomerPage,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}

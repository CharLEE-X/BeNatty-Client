package feature.admin.user.page

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

class AdminUserPageViewModel(
    userId: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToUserList: suspend () -> Unit,
) : BasicViewModel<
    AdminUserPageContract.Inputs,
    AdminUserPageContract.Events,
    AdminUserPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminUserPageContract.State(),
            inputHandler = AdminUserPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminUserPageEventHandler(
        onError = onError,
        goToUserList = goToUserList,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminUserPageContract.Inputs.Init(id = userId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

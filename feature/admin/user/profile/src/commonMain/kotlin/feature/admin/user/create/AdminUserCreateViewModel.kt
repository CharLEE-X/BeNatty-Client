package feature.admin.user.create

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

class AdminUserCreateViewModel(
    userId: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToUserList: suspend () -> Unit,
) : BasicViewModel<
    AdminUserCreateContract.Inputs,
    AdminUserCreateContract.Events,
    AdminUserCreateContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminUserCreateContract.State(),
            inputHandler = AdminUserCreateInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminUserCreateEventHandler(
        onError = onError,
        goToUserList = goToUserList,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminUserCreateContract.Inputs.Init(id = userId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

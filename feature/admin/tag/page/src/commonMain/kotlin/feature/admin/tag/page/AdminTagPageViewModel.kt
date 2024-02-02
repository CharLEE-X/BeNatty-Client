package feature.admin.tag.page

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

class AdminTagPageViewModel(
    userId: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToUserList: suspend () -> Unit,
) : BasicViewModel<
    AdminTagPageContract.Inputs,
    AdminTagPageContract.Events,
    AdminTagPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminTagPageContract.State(),
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
    eventHandler = AdminTagPageEventHandler(
        onError = onError,
        goToUserList = goToUserList,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminTagPageContract.Inputs.Init(id = userId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

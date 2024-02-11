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
    id: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToUserList: () -> Unit,
    goToUser: (String) -> Unit,
    goToTag: (String) -> Unit,
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
            initialState = AdminTagPageContract.State(
                screenState = if (id == null) {
                    AdminTagPageContract.ScreenState.New
                } else {
                    AdminTagPageContract.ScreenState.Existing
                }
            ),
            inputHandler = AdminTagPageInputHandler(),
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
        goToUser = goToUser,
        goToTag = goToTag,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminTagPageContract.Inputs.Init(id = id))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}


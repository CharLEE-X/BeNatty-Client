package feature.debug

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DebugViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    onGenerateUsersSuccess: suspend (String) -> Unit,
    onDeleteUsersSuccess: suspend (String) -> Unit,
) : BasicViewModel<
    DebugContract.Inputs,
    DebugContract.Events,
    DebugContract.State,
    >(
    config =
    BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = DebugContract.State(),
            inputHandler = DebugInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler =
    DebugEventHandler(
        onError = onError,
        onGenerateUsersSuccess = onGenerateUsersSuccess,
        onDeleteUsersSuccess = onDeleteUsersSuccess,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(DebugContract.Inputs.Init)
    }

    companion object {
        private val TAG = DebugViewModel::class.simpleName!!
    }
}

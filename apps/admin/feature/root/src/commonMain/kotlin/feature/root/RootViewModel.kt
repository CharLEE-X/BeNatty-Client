package feature.root

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class RootViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    RootContract.Inputs,
    RootContract.Events,
    RootContract.State,
    >(
    config =
    BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = RootContract.State(),
            inputHandler = RootInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler =
    RootEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(RootContract.Inputs.ObserveAuthState)
    }

    companion object {
        private val TAG = RootViewModel::class.simpleName!!
    }
}

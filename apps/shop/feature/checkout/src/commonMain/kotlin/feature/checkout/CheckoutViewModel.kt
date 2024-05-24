package feature.checkout

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

class CheckoutViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    CheckoutContract.Inputs,
    CheckoutContract.Events,
    CheckoutContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = CheckoutContract.State(),
            inputHandler = CheckoutInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = CheckoutEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(CheckoutContract.Inputs.Init)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

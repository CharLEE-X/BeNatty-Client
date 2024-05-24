package feature.shop.account.profile

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

class ProfileViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    ProfileContract.Inputs,
    ProfileContract.Events,
    ProfileContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = ProfileContract.State(),
            inputHandler = ProfileInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = ProfileEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(ProfileContract.Inputs.GetUserProfile)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

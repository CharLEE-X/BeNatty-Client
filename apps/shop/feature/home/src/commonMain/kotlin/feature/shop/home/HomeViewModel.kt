package feature.shop.home

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

class HomeViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    homeRoutes: HomeRoutes,
) : BasicViewModel<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = HomeContract.State(),
            inputHandler = HomeInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = HomeEventHandler(
        onError = onError,
        homeRoutes = homeRoutes,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(HomeContract.Inputs.Init)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
